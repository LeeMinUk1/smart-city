export interface Menu {
    label: string;
    icon?: string;
    activeIcon?: string;
    to?: string;
    expanded?: boolean;
    selected?: boolean;
    children?: Menu[];
    breadcrumb?: { label: string }[];
}

export const rootMenu: Menu = {
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
                    to: '/power-usage',
                    expanded: false
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
                    label: 'Q & A'
                },
                {
                    label: '공지사항'
                },
                {
                    label: '화상회의(줌링크)'
                }
            ]
        }
    ]
};

export const noticeMenu: Menu = {
    label: '공지사항',
    to: '/notice-list',
    expanded: false
};