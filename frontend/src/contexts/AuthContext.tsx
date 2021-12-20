import { createContext, ReactNode, useEffect, useState } from "react";
import AuthService from "../services/authService";
import jwt from 'jsonwebtoken';

type AuthContextType = {
  isAuthenticated: boolean;
  authenticatedUser: User | undefined;
  startSession: (token: TokenDTO) => void;
  endSession: () => void;
}

type User = {
  id: number;
  nome: string;
}

type TokenDTO = {
  token: string;
}

type TokenProps = {
  sub: string;
  exp: number;
  userId: number;
  userName: string;
}

type AuthContextProviderProps = {
  children: ReactNode;
}

export const AuthContext = createContext({} as AuthContextType);

export function AuthContextProvider(props: AuthContextProviderProps) {
  const [authenticatedUser, setAuthenticatedUser] = useState<User>();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  function startSession(tokenDTO: TokenDTO) {
    const token = tokenDTO.token;
    const claims = jwt.decode(token) as TokenProps;
    const user: User = {
      id: claims.userId,
      nome: claims.userName
    }

    AuthService.signIn(user, { token });

    setIsAuthenticated(true);
    setAuthenticatedUser(user);
  }

  function endSession() {
    AuthService.logout();
    setIsAuthenticated(false);
    setAuthenticatedUser(undefined);
  }

  useEffect(() => {
    const isAuthenticatedUser = AuthService.isAuthenticatedUser();

    if (isAuthenticatedUser) {
      const user: User = AuthService.refreshSession();
      setIsAuthenticated(true);
      setAuthenticatedUser(user);
      setIsLoading(false);
    } else {
      setIsLoading(prevIsLoading => false);
    }
  }, [])

  if (isLoading) {
    return null;
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        authenticatedUser,
        startSession,
        endSession
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );
}