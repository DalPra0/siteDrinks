package com.DalPra.drinkmenu.servlet;

import com.DalPra.drinkmenu.dao.DrinkDAO;
import com.DalPra.drinkmenu.model.Drink;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/add-drink")
public class AddDrinkServlet extends HttpServlet {
    private DrinkDAO drinkDAO;
    private Gson gson = new Gson();

    public void init() {
        drinkDAO = new DrinkDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Drink drink = gson.fromJson(sb.toString(), Drink.class);

        try {
            drinkDAO.addDrink(drink);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\":\"Drink added successfully!\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Error adding drink: " + e.getMessage() + "\"}");
        }
    }
}