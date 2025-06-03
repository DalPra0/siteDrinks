package com.DalPra.drinkmenu.servlet;

import com.DalPra.drinkmenu.dao.DrinkDAO;
import com.DalPra.drinkmenu.model.Drink;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/drinks")
public class ListDrinksServlet extends HttpServlet {
    private DrinkDAO drinkDAO;
    private Gson gson = new Gson();

    public void init() {
        drinkDAO = new DrinkDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String limitParam = request.getParameter("limit");
        List<Drink> drinks;

        if (limitParam != null) {
            try {
                int limit = Integer.parseInt(limitParam);
                drinks = drinkDAO.getLatestDrinks(limit);
            } catch (NumberFormatException e) {
                drinks = drinkDAO.getAllDrinks(); // fallback to all if limit is invalid
            }
        } else {
            drinks = drinkDAO.getAllDrinks();
        }

        out.print(gson.toJson(drinks));
        out.flush();
    }
}