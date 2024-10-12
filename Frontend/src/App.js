// App.js

import { Routes, Route } from "react-router-dom";
import React, { useState } from 'react';
import SignInSide from "./FrontEnd/login/SignInSide";
import Home from "./FrontEnd/others/Home";
import RegisterSide from "./FrontEnd/register/RegisterSide";
import MyBidding from "./FrontEnd/MyBidding";


function App() {
  // const [buyerId, setBuyerId] = useState(null);
  // const [productId, setProductId] = useState(null);

  return (
    <>
      <Routes>
        <Route
          path="/"
          element={<SignInSide />}
        />
        <Route
          path="/biddingPage"
          element={<Home />}
        />
        <Route
          path="/login"
          element={<SignInSide />}
        />
        <Route
          path="/register"
          element={<RegisterSide />}
        />
        <Route
          path="/biddingHistory"
          element={<MyBidding  />}
        />
      </Routes>
    </>
  );
}

export default App;
