import {HttpInterceptorFn} from '@angular/common/http';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem("token");

  const modifyReq = token ? req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  },) : req;
  return next(modifyReq);

};
