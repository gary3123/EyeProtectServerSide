package com.example.eyeprotext.InviteRoomWebSocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller("webScoketSystem")
public class InviteRoomWebSocketController {
    public class SystemController {
        //页面请求
        @GetMapping("/index/{userId}")
        public ModelAndView socket(@PathVariable String userId) {
            ModelAndView mav = new ModelAndView("/socket1");
            mav.addObject("userId", userId);
            return mav;
        }

        //推送数据接口
//        @ResponseBody
//        @RequestMapping("/socket/push/{cid}")
//        public Map pushToWeb(@PathVariable String cid, String message) {
//            Map<String,Object> result = new HashMap<>();
//            try {
//                WebSocketService.sendInfo(message, cid);
//                result.put("code", cid);
//                result.put("msg", message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
    }
}
