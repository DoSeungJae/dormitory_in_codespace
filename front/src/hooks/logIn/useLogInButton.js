import Button from '../../components/common/Button.js';
import {useState,useContext,useEffect} from 'react';
import InputForm from '../../components/common/InputForm.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import axios from 'axios';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';
import { toast } from 'react-toastify';
import ArticleContext from '../../components/article/ArticleContext.js';
import { getRelativeTime } from '../../modules/common/timeModule.js';

function UseLogInButton() {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [idError,setIdError]=useState(false);
    const [pwError,setPwError]=useState(false);
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const {article,setArticle}=useContext(ArticleContext);

    useEffect(()=>{
      if(Object.keys(article).length !==0){
          setSelectComponentIndex(5);
      }
    },[article])

    const getArticle = async (articleId) => {
      const path=`${process.env.REACT_APP_HTTP_API_URL}/article/${articleId}`;
      try{
        const response=await axios.get(path);
        return response.data;
      }catch(error){
        console.error(error);
      }
    }

    const buttonPressed = () => {
      if(id===''){
        setIdError(true);

      }
      if(pw===''){
        setPwError(true);
      }

      else{
        axios.post(`${process.env.REACT_APP_HTTP_API_URL}/user/logIn`,
        {email:id,passWord:pw})
        .then(response => {
          localStorage.setItem('token',response.data);
          if(localStorage.getItem("nextIndex")===null){
            window.location.reload();
          }
          else{
            const requiredIndex=parseInt(localStorage.getItem("nextIndex"));
            if(requiredIndex!==5){
              localStorage.setItem("index",requiredIndex);
              window.location.reload();
              localStorage.removeItem("nextIndex");
            }
            else{
              window.location.reload();
            }
            /*
            else{
              const articleId=localStorage.getItem("articleId");
              localStorage.removeItem("articleId");
              getArticle(articleId).then(article=>setArticle(article));
            }
              article 리다이렉트 기능은 구현이 어려우므로 나중에(아마도?) 구현
              */

          }
          
        })
        .catch(error => {
          let errorData = error.response.data;
          let loginRestrictionMessage = "LoginRestricted:expiredAt:";
          console.error(errorData);
          if (errorData.startsWith(loginRestrictionMessage)) {
            let expireTime = errorData.slice(loginRestrictionMessage.length);
            toast.error(`계정이 제재되어 ${getRelativeTime(expireTime)}까지 로그인할 수 없어요, 이메일을 확인하세요.`);
          } else {
            toast.error(errorData);
          }
          
        })
      }
      
    };
  
    return (
      <div className="container">
        <div className="row mt-3">
          <InputForm
            className="form-control form-control-sm"
            type="text"
            placeholder="아이디"
            value={id}
            onChange={(e) => setId(e.target.value)}
          />

          {idError && <small id="small" className="mt-2">아이디를 입력해주세요.</small>}
        </div>
        <div className="row mt-3">
          <InputForm
            className="form-control form-control-sm"
            type="password"
            placeholder="비밀번호"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
          />
          {pwError && <small id="small" className="mt-2">비밀번호를 입력해주세요.</small>}
        </div>
  
        <div className="d-grid mt-4 pb-4">
          <div onClick={buttonPressed} className="btn big btn-primary btn-sm">로그인</div>
        </div>
      </div>
    );
  }
  
  export default UseLogInButton;

