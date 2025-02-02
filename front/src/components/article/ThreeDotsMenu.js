import React,{useState,useContext}from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import axios from 'axios';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Swal from 'sweetalert2';
import HomeSelectContext from '../home/HomeSelectContext';
import { checkRestriction } from '../../modules/common/restrictionModule';

const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
  <h1
    href=""
    ref={ref}
    onClick={(e) => {
      e.preventDefault();
      onClick(e);
    }}
  >
    {children}
  </h1>
));

const ThreeDotsMenu = ({isWriterParam,articleParam,commentParam,setCommentsAltered}) => {
  const token=localStorage.getItem('token');
  const [isWriter,setIsWriter]=useState(0);
  const [article,setArticle]=useState("");
  const [comment,setComment]=useState("");
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

  const goToPostingPageInPatchMode = async () => {
    const handleSwalAlert = async () => {
      Swal.fire({
        title:"수정하시겠어요?",
        text:"그룹에 다른 사람이 참가한 경우에는 글을 수정할 때 기숙사, 카테고리를 변경할 수 없어요.",
        icon: "warning",
        showCancelButton:true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "그래도 수정할게요!",
        cancelButtonText:"취소",
      }).then((result)=>{
        if(result.isConfirmed){
          localStorage.setItem("toBePatchgedArticleId",article.id);
          localStorage.setItem("limitedPatch",1);
          setSelectComponentIndex(2);
        }

      })
    }

    const path=`${process.env.REACT_APP_HTTP_API_URL}/group/numMembers?groupId=${article.id}`;
    try{
      const response=await axios.get(path);
      const restricted = await checkRestriction("ARTICLE");
      if(restricted) {
        toast.error("글쓰기가 제재되었어요, 제한 내역을 확인하세요.");
        return;
      }
      if(response.data>1){
        handleSwalAlert(); 

      }
      else{
        localStorage.setItem("toBePatchgedArticleId",article.id);
        setSelectComponentIndex(2);
      }
    }
    catch(error){
      console.log(error);
    }


    }



  const handleSwal=async () => {
    const { value: fruit } = await Swal.fire({
      confirmButtonColor:"#FF8C00",
      title: "신고",
      confirmButtonText:"신고",
      cancelButtonText:"취소",
      input: "select",
      inputOptions: {
        도배글:"도배글",
        음란물:"음란물",
        상업적글:"상업적글",
        정치글:"정치글",
        노쇼:"노쇼",
        욕설:"욕설",
        비하:"비하"
      },
      inputPlaceholder: "신고 사유를 선택하세요.",
      showCancelButton: true,
      inputValidator: (value) => {
        return new Promise((resolve) => {
          if (value) {
            resolve();
            reportArticle(value);
          } else {
            resolve("신고 사유를 선택해주세요!");
          }
        });
      }
    });
  }

  const reportArticle = async (reportReason) => {
    try {
      const path = `${process.env.REACT_APP_HTTP_API_URL}/report/new`;
      let data={};
      if(article){
        data = {
          reportType:"ARTICLE",
          targetId:article.id,
          reason:reportReason
        };
      }
      else if(comment){
        data = {
          reportType:"COMMENT",
          targetId:comment.id,
          reason:reportReason
        };
      }

      const response = await axios.post(path, data,{
          headers: {
              'Authorization': `${token}`
          }
      });
      if(response.status===200){
        toast.success("신고가 접수되었어요.");
      } 
    } catch (error) {
        localStorage.setItem("nextIndex",5);
        toast.error("로그인 정보가 만료되었어요! 다시 로그인해주세요.");
        setSelectComponentIndex(8);
    }
  }

  const deleteTarget = async (token,article,comment) => {
    let path="";
    let isArticle=0;
    if(article){
      path=`${process.env.REACT_APP_HTTP_API_URL}/article/${article.id}`;
      isArticle=1;
    }
    else{
      path=`${process.env.REACT_APP_HTTP_API_URL}/comment/${comment.id}`
    }
    try {
      const response = await axios.delete(path, {
          headers: {
              'Authorization': `${token}`
          }
      });
      if(response.status===200){
        if(isArticle){
          const prevPage=(localStorage.getItem("nextIndex"));
          if(prevPage!=null){
            localStorage.setItem("index",parseInt(prevPage));
          }
          window.location.reload();
        }
        else{
          setCommentsAltered(1);
        }
 
      } 
    } catch (error) {
        const errMsg=error.response.data;
        if(errMsg=="ArticleWithAProceedingGroupCannotBeDeleted"){
          toast.error("글을 삭제하려면 그룹을 종료해야해요.")
        }
        else{
          toast.error("글을 삭제하지 못했어요! 다시 시도해주세요.");
        }
    }
  }

  const menuItems = {
    0: [
      { type : 'item', eventKey: "1", text: "신고", action: () => handleSwal()},
      { type: 'divider' },
      { type : 'item', eventKey: "2", text: "URL 공유", action: () => console.log(1) },
    ],
    1: [
      commentParam ? null : 
      { type : 'item', eventKey: "1", text: "수정", action: () => goToPostingPageInPatchMode()},
      { type: 'divider' },
      { type : 'item', eventKey: "2", text: "삭제", action: () => deleteTarget(token,article,comment) },
      { type: 'divider' },
      { type : 'item', eventKey: "3", text: "URL 공유", action: () => alert('Action 3-2 executed') },
    ].filter(Boolean),
  };

  const handleToggle = () => {
    setIsWriter(isWriterParam === 1 ? 1 : 0); // 상태 토글
    setArticle(articleParam);
    setComment(commentParam);
  };

  return (
    <Dropdown onToggle={handleToggle}>
      <Dropdown.Toggle style={{ color: 'black' }} as={CustomToggle} id="dropdown-autoclose-true">
        <ThreeDots/>
      </Dropdown.Toggle>

      <Dropdown.Menu>
        {menuItems[isWriter].map((item,index) =>{
          if(item.type==='divider'){
            return <Dropdown.Divider key={index}/>;
          }
          return (
            <Dropdown.Item
              eventKey={item.eventKey}
              key={index}
              onClick={item.action}
            >
              {item.text}
            </Dropdown.Item>
          )

        })}
      </Dropdown.Menu>

    </Dropdown>
  );
};
export default ThreeDotsMenu;
