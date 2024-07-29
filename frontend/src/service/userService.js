import axios from "axios";
const API_URL = "http://localhost:8080";

class UserService {
    saveUser(user) {
        return axios.post(API_URL + "/signUp", user);
    }
}

export default new UserService;