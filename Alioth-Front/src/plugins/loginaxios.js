import axios from "axios";

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_SERVER_BASE_URL
});

axiosInstance.interceptors.request.use(
  config => {
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    console.log("베어러토큰 : " + config.headers.Authorization);
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  response => {
    return response;
  },
  async error => {
    const originalRequest = error.config;

    if (error.response.status === 401 && !originalRequest._retry) {
      if (error.response.data.message === "토큰을 재발행 합니다.") {
        localStorage.setItem('accessToken', error.response.headers["access-token"]);
        localStorage.setItem('refreshToken', error.response.headers["fresh-token"]);
        originalRequest.headers.Authorization = `Bearer ${error.response.data.result}`;
        return axiosInstance(originalRequest);
      } else {
        localStorage.removeItem('accessToken');
      }
      originalRequest._retry = true;
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
