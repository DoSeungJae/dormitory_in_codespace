
const NotiPreview = ({notiList}) => {
    
    return (
        <div className="preview">
            {notiList && notiList.map((noti,index)=>(
                <div key={index} className="noti-item" 
                    onClick={console.log()}>
                    {index}
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;