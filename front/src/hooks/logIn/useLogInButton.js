import Button from '../../components/common/Button.js';
import {useState,useContext,useEffect} from 'react';
import InputForm from '../../components/common/InputForm.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import axios from 'axios';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';
import { toast } from 'react-toastify';

function UseLogInButton() {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [idError,setIdError]=useState(false);
    const [pwError,setPwError]=useState(false);
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    
    const buttonPressed = () => {
      if(id===''){
        setIdError(true);

      }
      if(pw===''){
        setPwError(true);
      }

      else{
        axios.post('https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/user/logIn',
        {email:id,passWord:pw})
        .then(response => {
          console.log(response.data);
          localStorage.setItem('token',response.data);
          if(localStorage.getItem("nextIndex")===null){
            window.location.reload();
            setSelectComponentIndex(0);
          }
          else{
            const requiredIndex=parseInt(localStorage.getItem("nextIndex"));
            localStorage.setItem("index",requiredIndex);
            window.location.reload();
            localStorage.removeItem("nextIndex");
          }
          
          
        })
        .catch(error => {
          console.error(error.response.data);
          toast.error(error.response.data);
        })
      }
      
    };
  
    return (
      <div className="container">
        <div className="row mt-5">
          <InputForm
            type="text"
            placeholder="아이디"
            value={id}
            onChange={(e) => setId(e.target.value)}
          />

          {idError && <small id="small" className="mt-2">아이디를 입력해주세요.</small>}
        </div>
        <div className="row mt-3">
          <InputForm
            type="password"
            placeholder="비밀번호"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
          />
          {pwError && <small id="small" className="mt-2">비밀번호를 입력해주세요.</small>}
        </div>
  
        <div className="d-grid mt-4 pb-4">
          <Button onClick={buttonPressed}>로그인</Button>
        </div>
      </div>
    );
  }
  
  export default UseLogInButton;

