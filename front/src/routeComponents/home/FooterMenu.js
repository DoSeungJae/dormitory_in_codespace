
function FooterMenu ({selectMenu,saveScrollState,isEndPage,dorId,scrollPosition,page,
                        selectComponentIndex,setSelectComponentIndex}){
    const svgMap = {
        '홈': (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 26" 
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M2 15L12 3l10 12v15a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1v-15z" />
              <path d="M9 30V15h6v15" />
            </svg>
        ),
        '내 글': (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <line x1="3" y1="6" x2="17" y2="6" />
              <line x1="3" y1="12" x2="21" y2="12" />
              <line x1="3" y1="18" x2="15" y2="18" />
            </svg>
        ),
        '글쓰기': (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M18.5 3a2.5 2.5 0 0 1 3.5 3.5L7.5 21H3v-4.5L18.5 3z" />
              <path d="M15 5L21 11" />
            </svg>
        ),
        '알림': (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="26"
              height="26"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M12 22c1.1 0 2-.9 2-2H10c0 1.1.9 2 2 2z" />
              <path d="M18 16v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.93 6 11v5l-2 2v1h16v-1l-2-2z" />
            </svg>
        ),
      };
    return (
        <div className='App'>
            <div className="bottom-menu">
                {['홈','내 글', '글쓰기', '알림'].map((item, i) => (
                <div 
                    key={i}
                    className="menu-item"
                    style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}
                    
                    onClick={() => {
                        if(item!='홈'){
                            selectMenu(item);
                            item=null;                
                        }
                        else{
                            if(selectComponentIndex===0){
                                window.location.reload();
                            }
                            else{
                                setSelectComponentIndex(0);
                            }
                        }
                    }}
                >
                    {svgMap[item]}
                    {<div style={{fontSize: '1.7vh',paddingTop:'0.5vh'}}>{item}</div>}
                </div>
                ))}
            </div>
        </div>
    )
    
}

export default FooterMenu;
