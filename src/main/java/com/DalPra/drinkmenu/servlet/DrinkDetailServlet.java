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

@WebServlet("/api/drinks-detail")
public class DrinkDetailServlet extends HttpServlet {
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

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Drink drink = drinkDAO.getDrinkById(id);
                if (drink != null) {
                    out.print(gson.toJson(drink));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Drink não encontrado\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID inválido\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID é obrigatório\"}");
        }
        out.flush();
    }

    // Atualizar drink (PUT)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Lê o JSON do corpo da requisição
            BufferedReader reader = request.getReader();
            Drink updatedDrink = gson.fromJson(reader, Drink.class);

            if (updatedDrink == null || updatedDrink.getId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Dados do drink inválidos ou id não fornecido\"}");
                return;
            }

            // Atualiza no banco
            boolean success = drinkDAO.updateDrink(updatedDrink);
            if (success) {
                out.print(gson.toJson(updatedDrink));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Drink não encontrado para atualizar\"}");
            }
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"JSON inválido\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro ao atualizar drink\"}");
            e.printStackTrace();
        }
        out.flush();
    }

    // Deletar drink (DELETE)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                boolean deleted = drinkDAO.deleteDrink(id);
                if (deleted) {
                    out.print("{\"message\": \"Drink deletado com sucesso\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Drink não encontrado para deletar\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID inválido\"}");
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Erro ao deletar drink\"}");
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID é obrigatório para deletar\"}");
        }
        out.flush();
    }
}

