import Footer from "./Footer";
import Header from "./Header";
import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import jwt_decode from "jwt-decode";
import Cookies from "js-cookie";
import { NotificationProvider } from "./notifications/NotificationContext";
import Notifications from "./notifications/Notifications";

const Layout = () => {
  const jwtCookie = Cookies.get("jwt");
  const [user, setUser] = useState(null);
  const [update, setUpdate] = useState(false);

  useEffect(() => {
    if (jwtCookie) {
      setUser(jwt_decode(jwtCookie));
    }
  }, [jwtCookie, update]);

  return (
    <NotificationProvider>
      <Header user={user} setUser={setUser} />
      <Outlet context={{ user, setUser, update, setUpdate }} />
      <Footer />
      <Notifications />
    </NotificationProvider>
  );
};

export default Layout;
