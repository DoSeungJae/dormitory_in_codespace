import React from 'react';
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 

const Button = ({onClick,children}) => {
    return (
        <button class="btn btn-primary" type="button" onClick={onClick}>
            {children}
        </button>
    );
};

export default Button;
