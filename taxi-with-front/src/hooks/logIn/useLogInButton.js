import Button from '../../components/common/Button.js';
import {useState} from 'react';
import InputForm from '../../components/common/InputForm.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';

function UseLogInButton(){
    const [id,setId]=useState('');
    const [pw,setPw]=useState('');

    const requestHTTP = () => {

    }
    const buttonPressed = () => { //Request Http
        console.log(id,pw);
    }

    return (

        <div className="container">
        <div className="row mt-5">
            <div className="col">
                <input 
                    type="text" //text or password
                    class="form-control" 
                    placeholder="아이디"
                    value={id}
                    onChange={e => setId(e.target.value)}
                />
            </div>
        </div>
        <div className="row mt-3">
            <div className="col">
                <input 
                    type="password" //text or password
                    class="form-control" 
                    placeholder="비밀번호"
                    value={pw}
                    onChange={e => setPw(e.target.value)}
                />
            </div>
        </div>

        <div class="d-grid mt-4 pb-4">
            <Button onClick={buttonPressed}>로그인</Button>
        </div>
    </div>

    );
}

export default UseLogInButton;


