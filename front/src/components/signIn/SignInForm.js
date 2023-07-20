import React, { useRef, useEffect, useState, forwardRef } from 'react';

const SignInForm = forwardRef(({ type, placeholder, value, onChange }, ref) => {
  const inputRef = useRef(null);
  const [isInputFocused, setIsInputFocused] = useState(false);

  useEffect(() => {
    const handleFocusChange = () => {
      setIsInputFocused(inputRef.current === document.activeElement);
    };

    document.addEventListener('focusin', handleFocusChange);
    document.addEventListener('focusout', handleFocusChange);

    return () => {
      document.removeEventListener('focusin', handleFocusChange);
      document.removeEventListener('focusout', handleFocusChange);
    };
  }, []);

  useEffect(() => {
    if (ref) {
      ref.current = inputRef.current;
    }
    else{
        ref.current=null;
    }
  }, [ref]);

  return (
    <div>
      <input
        type={type}
        className="form-control"
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        ref={inputRef}
      />
    </div>
  );
});

export default SignInForm;