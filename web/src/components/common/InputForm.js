import {React,useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js';


function InputForm({ placeholder, type, value, onChange }) {
    return (
      <div  className="col">
        <input
          type={type}
          className="form-control"
          placeholder={placeholder}
          value={value}
          onChange={onChange}
        />
      </div>
    );
  }

export default InputForm;


//인폿 폼을 label 안에 넣어 작성하는 것이 관례임 - ChatGPT`