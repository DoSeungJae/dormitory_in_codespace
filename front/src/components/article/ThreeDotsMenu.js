import React,{useState,useContext}from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AlertContext from '../common/AlertContext';

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

const ThreeDotsMenu = ({isWriterParam,articleParam}) => {
  const navigate = useNavigate();
  const token=localStorage.getItem('token');
  const [isWriter,setIsWriter]=useState(0); //초기값을 isWriterParam으로 설정할 시 에러 발생 -> 렌더링 안됨.
  const [article,setArticle]=useState("");
  const notify= (message) => toast(message);

  
  
  const deleteArticle = async (token,article) => {
    //alert로 한 번 더 확인하는 기능 추가해야함.

    try {
      const response = await axios.delete(`http://localhost:8080/api/v1/article/${article.id}`, {
          headers: {
              'Authorization': `${token}`
          }
      });
      if(response.status===200){
        navigate("/",{state:{type:"success",message:"글을 잘 삭제했어요."}});      
      } 
    } catch (error) {// jwt 무효 
        toast.error("글을 삭제하지 못했어요! 다시 시도해주세요.");
        console.error('An error occurred:', error);
    }
  }

  const menuItems = {
    0: [
      { eventKey: "1", text: "신고", action: () => notify("!23123123!!!")},
      { eventKey: "2", text: "URL 공유", action: () => console.log(1) },
    ],
    1: [
      { eventKey: "1", text: "수정", action: () => navigate("/article/modify",{state:{articleId:article.id}}) },
      { eventKey: "2", text: "삭제", action: () => deleteArticle(token,article) },
      { eventKey: "3", text: "URL 공유", action: () => alert('Action 3-2 executed') },
    ],
  };



  const handleToggle = () => {
    setIsWriter(isWriterParam === 1 ? 1 : 0); // 상태 토글
    setArticle(articleParam); //필요 없는듯?
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
