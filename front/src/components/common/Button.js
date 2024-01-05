import React from 'react';
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 

const Button = ({onClick,children}) => {

    return (
        <button className="btn btn-dark" type="button" onClick={onClick}>
            {children}
        </button>
    );
};

export default Button;
