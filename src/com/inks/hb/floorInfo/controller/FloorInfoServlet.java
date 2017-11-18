package com.inks.hb.floorInfo.controller;

import com.google.gson.Gson;
import com.inks.hb.common.PojotoGson;
import com.inks.hb.floorInfo.pojo.FloorInfo;
import com.inks.hb.floorInfo.service.FloorInfoService;
import com.inks.hb.floorInfo.service.FloorInfoServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 与表格相关的全部操作
 * 状态标志： make 0重载 1新增 2修改 3搜索 4删除
 */
@WebServlet(name = "/FloorInfoServlet", value = "/FloorInfoServlet")
public class FloorInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 响应输出流
        PrintWriter out = response.getWriter();

        // 当前页码
        int page = Integer.parseInt(request.getParameter("page"));

        // 每页的数据量
        int limit = Integer.parseInt(request.getParameter("limit"));

        // 状态标志 make 0重载 1新增 2修改 3搜索 4删除
        int make = Integer.parseInt(request.getParameter("make"));

        // 调用service
        FloorInfoService service = new FloorInfoServiceImpl();

        // 默认输出信息
        String code = "0"; //状态码
        String msg = "数据查询正常"; //状态信息
        String count = ""; //数据总数
        ArrayList<FloorInfo> list = null; //数据内容

        int floorId;
        String floorName;
        FloorInfo floorInfo = new FloorInfo();

        try {
            switch (make) {
                case 1:
                    floorName = request.getParameter("floorName");
                    service.insertFloorInfo(floorName);
                    break;
                case 2:
                    floorId = Integer.parseInt(request.getParameter("floorId"));
                    floorName = request.getParameter("floorName");
                    floorInfo = new FloorInfo(floorId, floorName);
                    service.updateFloorInfo(floorInfo);
                    break;
                case 3:
                    floorName = request.getParameter("floorName");
                    floorInfo = service.query(floorName);
                    break;
                case 4:
                    floorId = Integer.parseInt(request.getParameter("floorId"));
                    service.deleteFloorInfo(floorId);
                    break;
            }
            if (make == 3) {
                list = new ArrayList<>();
                list.add(floorInfo);
                count = "1";
            } else {
                list = service.query(page, limit);
                count = String.valueOf(service.queryFloorInfoNum());
            }
        } catch (SQLException e) {
            code = "1";
            msg = "数据查询出现异常";
            e.printStackTrace();
        } finally {
            PojotoGson pojotoGson = new PojotoGson(code, msg, count, list);
            Gson gson = new Gson();
            out.print(gson.toJson(pojotoGson));
        }
    }
}
