import React, { useState, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import InputForm from '../../components/common/InputForm';
import SignInForm from '../../components/signIn/SignInForm';
import Button from '../../components/common/Button';
import axios from 'axios';


const UseSignInButton = () => {
  const [eMail, setEMail] = useState('');
  const [pw, setPw] = useState('');
  const [pw2, setPw2] = useState('');
  const [tel, setTel] = useState('');
  const [nick, setNick] = useState('');

  const refMail = useRef(null);
  const refPw = useRef(null);
  const refPw2 = useRef(null);
  const refTel = useRef(null);
  const refNick = useRef(null);

  const rex_email = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const rex_pw = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9!@#$%^&*()]{8,16}$/;
  const rex_tel = /^010\d{8}$/;

  const rex_nick = /^[a-zA-Z0-9ㄱ-ㅎ]{1,8}$/;

  const isMailFocused = refMail.current === document.activeElement;
  const isPwFocused = refPw.current === document.activeElement;
  const isPw2Focused = refPw2.current === document.activeElement;
  const isTelFocused = refTel.current === document.activeElement;
  const isNickFocused = refNick.current === document.activeElement;

  const buttonPressed = () => {
    if(rex_email.test(eMail) &&
        rex_pw.test(pw) &&
        pw===pw2 &&
        rex_tel.test(tel) &&
        rex_nick.test(nick)
        ){
            axios.post('http://localhost:8080/api/v1/user/join',
            {eMail:eMail,passWord:pw,nickName:nick})
            .then(response => {
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            })

            
        }
    else{
        console.log("error");
        
    }
  }
  




  return (
    <div className="SignInPage">
      <div className="container p-4">
        <h3 className="Title mt-3">회원정보를 입력해주세요!</h3>
        <label className="row mt-3">
          <h6 className="col" id="signInForm">
            이메일
          </h6>
          <div className="col">
            {eMail.length !== 0 && rex_email.test(eMail) === false && isMailFocused === false &&  (
              <small id="signInSmall">양식에 맞지 않아요!</small>
            )}
          </div>
          <SignInForm
            type="text"
            placeholder="영문, 숫자로 6~12자 사이"
            value={eMail}
            onChange={(e) => setEMail(e.target.value)}
            ref={refMail}
          />
        </label>

        <label className="row mt-3">
          <h6 className="col" id="signInForm">
            비밀번호
          </h6>
          <div className="col">
            {pw.length !== 0 && rex_pw.test(pw) === false && isPwFocused === false && (
              <small id="signInSmall">양식에 맞춰주세요!</small>
            )}
          </div>
          <SignInForm
            type="password"
            placeholder="영문, 숫자 (특수문자)로 8~16자 사이"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
            ref={refPw}
          />
        </label>

        <label className="row mt-3">
          <h6 className="col" id="signInForm">
            비밀번호 확인
          </h6>
          <div className="col">
            {pw2.length !== 0 && pw !== pw2 && isPw2Focused === false &&  (
              <small id="signInSmall">일치하지 않아요!</small>
            )}
          </div>
          <SignInForm
            type="password"
            placeholder="한 번 더 입력해주세요! "
            value={pw2}
            onChange={(e) => setPw2(e.target.value)}
            ref={refPw2}
          />
        </label>

        <label className="row mt-3">
          <h6 className="col" id="signInForm">
            전화번호
          </h6>
          <div className="col">
            {tel.length !== 0 && rex_tel.test(tel) === false && isTelFocused === false && (
              <small id="signInSmall">양식에 맞지 않아요!</small>
            )}
          </div>
          <SignInForm
            type="text"
            placeholder="'-'가 필요없어요!"
            value={tel}
            onChange={(e) => setTel(e.target.value)}
            ref={refTel}
          />
        </label>

        <label className="row mt-3">
          <h6 className="col" id="signInForm">
            닉네임
          </h6>
          <div className="col">
            {nick.length !== 0 && rex_nick.test(nick) === false && isNickFocused === false && (
              <small id="signInSmall">양식에 맞지 않아요!</small>
            )}
          </div>
          <SignInForm
            type="text"
            placeholder="특수문자 없이 8자리 이하"
            value={nick}
            onChange={(e) => setNick(e.target.value)}
            ref={refNick}
          />
        </label>
        

        <div className="row mt-5">
            <Button onClick={buttonPressed}>회원가입</Button>
        </div>

      </div>
    </div>
  );
};

export default UseSignInButton;