import React,{useState,useContext}from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Swal from 'sweetalert2';
import HomeSelectContext from '../home/HomeSelectContext';

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

const ThreeDotsMenu = ({isWriterParam,articleParam,commentParam}) => {
  const navigate = useNavigate();
  const token=localStorage.getItem('token');
  const [isWriter,setIsWriter]=useState(0);
  const [article,setArticle]=useState("");
  const [comment,setComment]=useState("");
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

  const goToPostingPageInPatchMode = () => {
    localStorage.setItem("toBePatchgedArticleId",article.id);
    console.log(localStorage.getItem("toBePatchgedArticleId"));
    setSelectComponentIndex(2);
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
      const path = `http://localhost:8080/api/v1/report/new`;
      let data={};
      if(article){
        data = {
          articleId:article.id,
          reason:reportReason
        };
      }


      else if(comment){
        console.log(comment);
        data = {
          commentId:comment.id,
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
        console.log(response.data);
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
      path=`http://localhost:8080/api/v1/article/${article.id}`;
      isArticle=1;
    }
    else{
      path=`http://localhost:8080/api/v1/comment/${comment.id}`
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
          localStorage.setItem("index",5);
          window.location.reload();
        }
 
      } 
    } catch (error) {
        toast.error("글을 삭제하지 못했어요! 다시 시도해주세요.");

    }
  }

  const menuItems = {
    0: [
      { eventKey: "1", text: "신고", action: () => handleSwal()},
      { eventKey: "2", text: "URL 공유", action: () => console.log(1) },
    ],
    1: [
      commentParam ? null : { eventKey: "1", text: "수정", action: () => goToPostingPageInPatchMode()},
      { eventKey: "2", text: "삭제", action: () => deleteTarget(token,article,comment) },
      { eventKey: "3", text: "URL 공유", action: () => alert('Action 3-2 executed') },
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
        {menuItems[isWriter].map((item, index) => (
        <Dropdown.Item 
          eventKey={item.eventKey} 
          key={index} 
          onClick={item.action} 
        >
          {item.text}
        </Dropdown.Item>
      ))}
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default ThreeDotsMenu;
