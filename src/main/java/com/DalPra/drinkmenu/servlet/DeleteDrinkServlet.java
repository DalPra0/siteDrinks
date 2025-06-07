package com.DalPra.drinkmenu.servlet;

import com.DalPra.drinkmenu.dao.DrinkDAO;
import com.DalPra.drinkmenu.model.Drink;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/delete-drink")
public class DeleteDrinkServlet extends HttpServlet {
    private DrinkDAO drinkDAO;

    public void init() {
        drinkDAO = new DrinkDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                boolean success = drinkDAO.deleteDrink(id);
                if (success) {
                    response.getWriter().write("{\"message\": \"Drink deletado com sucesso\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Drink não encontrado\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"ID inválido\"}");
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Erro no banco de dados\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID é obrigatório\"}");
        }
    }
}

