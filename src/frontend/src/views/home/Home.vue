<template src="./Home.html"/>

<script lang="ts">
    import {Vue, Options, Ref} from 'vue-property-decorator';
    // import {Menu, rootMenu as Root} from '../Menu';
    import {useRouter} from "vue-router";
    import {inject} from "vue";
    import {useAuthStore} from "@/store/auth";
    import TieredMenu from "primevue/tieredmenu";
    import ProgressBar from "primevue/progressbar";
    import * as qnaApi from "@/api/qna-api";
    declare function createUnityInstance(a: any, b: any, c: any): any;

    @Options({
        components: {ProgressBar}
    })
    export default class Home extends Vue {
        @Ref() readonly userMenu!: TieredMenu;
        @Ref() readonly detailMenu!: TieredMenu;
        @Ref() readonly canvas!: HTMLCanvasElement;

        private router = useRouter();
        authStore = useAuthStore();

        rootMenu: Menu = {
            label: '빌딩', children: [
                {
                    label: '모니터링',
                    icon: 'main_menu_icon_monitoring',
                    activeIcon: 'main_menu_icon_monitoring_active',
                    expanded: false,
                    children: [
                        {
                            label: '기상/대기오염 현황',
                            expanded: false,
                            children: [{
                                label: '기상청 초단기실황/대기오염 정보'
                            }]
                        },
                        {
                            label: '공조기/지열 설비 현황',
                            expanded: false,
                            children: [
                                {
                                    label: '공조기',
                                    expanded: false,
                                    children: [
                                        {
                                            label: '공조기(AHU-1, AHU-2) 운전분석 정보'
                                        },
                                        {
                                            label: '관제점 추이분석 정보'
                                        }
                                    ]
                                },
                                {
                                    label: '지열시스템',
                                    expanded: false,
                                    children: [
                                        {label: '성능분석 정보'},
                                        {label: '운전분석 정보'},
                                        {label: '관제점 추이분석 정보'}
                                    ]
                                }
                            ]
                        },
                        {
                            label: '태양광 발전',
                            expanded: false,
                            children: [
                                {label: 'SG 운영분석 정보'}
                            ]
                        },
                        {
                            label: '전력 수급 현황',
                            expanded: false,
                            children: [
                                {label: '전력수급 현황 정보'}
                            ]
                        },
                        {
                            label: '최대 수요 전력',
                            expanded: false,
                            children: [
                                {label: '최대 수요전력 정보'}
                            ]
                        },
                        {
                            label: '전력 사용량',
                            expanded: false,
                            children: [
                                {label: '건물별 전력 사용량'},
                                {label: '층별 전력 사용량'},
                                {label: '실별 전력 사용량'},
                                {label: '전력 사용 패턴'},
                            ]
                        }
                    ]
                }, {
                    label: 'A.I. 기반 예측',
                    icon: 'main_menu_icon_ai',
                    activeIcon: 'main_menu_icon_ai_active',
                    expanded: false,
                    children: [
                        {
                            label: '공조기/지열 설비 성능 예측'
                        },
                        {
                            label: '태양광 발전량 예측'
                        },
                        {
                            label: '전력 수급 예측'
                        },
                        {
                            label: '최대수요전력 예측'
                        },
                        {
                            label: '전력 사용량 예측'
                        },
                        {
                            label: 'A.I. 기반예측모델 설명'
                        }
                    ]
                }, {
                    separator: true
                }, {
                    label: 'A.I. 모델 설계',
                    icon: 'main_menu_btn_icon_ai_model',
                    activeIcon: 'main_menu_btn_icon_ai_model_active',
                    expanded: false,
                    children: [
                    ]
                }, {
                    label: '협업 시스템',
                    icon: 'main_menu_btn_icon_collaboration',
                    activeIcon: 'main_menu_btn_icon_collaboration_active',
                    expanded: false,
                    children: [
                        {
                            label: 'Q & A',
                            to: '/qna'
                        },
                        {
                            label: '공지사항',
                            to: '/notice-list'
                        },
                        {
                            label: '화상회의(줌링크)'
                        }
                    ]
                }
            ]
        };

        wholeCity = false;
        selectedDong = '';
        dongList = [];
        home: { label: string } = {label: this.rootMenu.label!};
        breadcrumb: { label: string }[] = [];

        userMenuItems = [
            {
                label: '로그아웃',
                icon: 'las la-sign-out-alt',
                command: () => {
                    window.location.href = '/';
                }
            },
            {
                separator: true
            },
            // {
            //     label: '관리',
            //     icon: 'las la-sign-out-alt',
            //     command: () => {
            //         this.managerMenu = true;
            //         this.router.push('/user');
            //     }
            // }
        ];
        detailMenuItems: Menu[] = [];
        currentView: Menu | null = null;

        managerMenu = true;
        collaborationMenu = false;
        private selectedMenu!: Menu;

        private emitter: any;
        private buildUrl = "siheungsi_out_allbuildings/Build";
        // private loaderUrl = this.buildUrl + "/siheungsi_out_allbuildings.loader.js";
        private config = {
            dataUrl: this.buildUrl + "/siheungsi_out_allbuildings.data",
            frameworkUrl: this.buildUrl + "/siheungsi_out_allbuildings.framework.js",
            codeUrl: this.buildUrl + "/siheungsi_out_allbuildings.wasm",
            streamingAssetsUrl: "StreamingAssets",
            companyName: "DefaultCompany",
            productName: "JWD",
            productVersion: "0.1",
            showBanner: this.unityShowBanner,
        };
        unityLoadingProgress = 0;
        unityLoaded = false;
        unityInstance: any;

        unreadQnaCount = 0;

        private unityShowBanner(msg: any, type: any) {
            console.error('Unity:', msg);
        }

        expandMenu(menu: Menu, parentMenu: Menu) {
            parentMenu.children?.forEach(menu => menu.expanded = false);
            menu.expanded = true;
        }

        openView(view: Menu) {
            console.log(view);
            this.currentView = view;
            this.breadcrumb = view.breadcrumb!;

            view.selected = true;
            if (this.selectedMenu) {
                this.selectedMenu.selected = false;
            }
            this.selectedMenu = view;

            if (view.children) {
                this.detailMenuItems = view.children;
            }

            if (view.to) {
                this.router.push(view.to);
            } else {
                if (view.label === '건물별 전력 사용량') {
                    this.unityInstance.SendMessage('ScriptManager','usingBemsBuildingTotal_jsCall');
                } else if (view.label === '층별 전력 사용량') {
                    this.unityInstance.SendMessage('ScriptManager', 'usingBemsBuilding_jsCall');
                }
            }
        }

        openPage(view: string) {
            this.router.push(view);
        }

        mounted() {
            if (this.authStore.isAdmin) {
                this.rootMenu.children!.push({
                    label: '관리자',
                    icon: 'main_menu_icon_monitoring',
                    expanded: false,
                    children: [{
                        label: '사용자 관리',
                        to: '/user'
                    }, {
                        label: '로그인 이력',
                        to: '/login-history'
                    }, {
                        label: '공지사항 관리',
                        to: '/notice-management'
                    }]
                });
            }

            this.emitter = inject('emitter');

            let breadcrumb: { label: string }[] = [];
            this.rootMenu.children?.forEach(c1 => {
                if (c1.label) {
                    breadcrumb.push({label: c1.label!});
                    c1.children?.forEach(c2 => {
                        breadcrumb.push({label: c2.label!});
                        if (c2.children) {
                            c2.children?.forEach(c3 => {
                                breadcrumb.push({label: c3.label!});
                                c3.breadcrumb = breadcrumb.slice();
                                breadcrumb.pop();
                            });
                        } else {
                            c2.breadcrumb = breadcrumb.slice();
                        }
                        breadcrumb.pop();
                    });
                    breadcrumb.pop();
                }
            });

            this.unityLoadingProgress = 0;
            this.unityLoaded = false;

            createUnityInstance(this.canvas, this.config, (progress: any) => {
                console.log(100 * progress + "%");
                this.unityLoadingProgress = 100 * Math.round(progress);
            }).then((unityInstance: any) => {
                this.unityInstance = unityInstance;
                this.unityLoadingProgress = 100;
                this.unityLoaded = true;
            }).catch((message: any) => {
                console.error(message);
                // alert(message);
            });

            const emitter: any = inject('emitter');
            emitter.on('full-screen-event', () => {
                this.unityInstance.SetFullscreen(1);
            });

            qnaApi.getUnreadCount(this.authStore.user.id).then(response => {
                this.unreadQnaCount = response.data;
            }).catch(error => {
                console.log(error);
            });
        }

        changeToUserMenu() {
            this.managerMenu = false;
            this.router.push('/home/building/dashboard')
        }

        toggleUserMenu(event: MouseEvent) {
            this.userMenu.toggle(event);
        }

        toggleDetailMenu(event: MouseEvent) {
            this.detailMenu.toggle(event);
        }

        fullScreen() {
            this.emitter.emit('full-screen-event');
        }
    }

    export interface Menu {
        label?: string;
        icon?: string;
        activeIcon?: string;
        to?: string;
        expanded?: boolean;
        selected?: boolean;
        children?: Menu[];
        breadcrumb?: { label: string }[];
        command?: () => void;
        separator?: boolean;
    }
</script>

<style lang="scss" src="./Home.scss"/>
