import React, { useState } from 'react';


function UseStateHome() {

    const [isMenuOpen, setIsMenuOpen] = useState(false);

    return (
        <div className="App">
            <header className="App-header">
                {/* Add your header content here */}
            </header>

            <main onClick={() => setIsMenuOpen(false)}>
                {/* Add your main content here */}
            </main>

            <footer 
              className={`App-footer ${isMenuOpen ? 'open' : ''}`} 
              onClick={() => setIsMenuOpen(!isMenuOpen)}
            >
                {/* Footer will be a slide-up menu */}
                Menu Slide Content Here
            </footer>
        </div>
    );
}

export default UseStateHome;
