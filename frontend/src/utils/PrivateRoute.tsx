// src/utils/PrivateRoute.js
import React, { ReactElement } from 'react';
import { Navigate } from 'react-router-dom';

interface PrivateRouteProps {
  element: ReactElement;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ element }) => {
  const isAuthenticated = !!sessionStorage.getItem('token');

  return isAuthenticated ? element : <Navigate to="/" />;
};

export default PrivateRoute;