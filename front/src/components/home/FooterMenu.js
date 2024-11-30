import GroupsIcon from '@mui/icons-material/Groups';
import HomeIcon from '@mui/icons-material/Home';
import NotificationsNoneOutlinedIcon from '@mui/icons-material/NotificationsNoneOutlined';
import NotesIcon from '@mui/icons-material/Notes';

function FooterMenu ({selectMenu,saveScrollState,isEndPage,dorId,scrollPosition,page,
                        selectComponentIndex,setSelectComponentIndex}){
    const iconMap = {
        '홈': <HomeIcon fontSize='medium'/>,
        '내 정보': <NotesIcon fontSize='medium'/>,
        '알림': <NotificationsNoneOutlinedIcon fontSize='medium'/>,
        '내 그룹':<GroupsIcon fontSize="medium"/>,
      };
    return (
        <div className='App'>
            <div className="bottom-menu">
                {['홈','내 그룹','내 정보', '알림'].map((item, i) => (
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
                    {iconMap[item]}
                    {<div style={{fontSize: '1.7vh',paddingTop:'0.5vh'}}>{item}</div>}
                </div>
                ))}
            </div>
        </div>
    )
    
}

export default FooterMenu;
