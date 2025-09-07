/*
 * The MIT License
 *
 * Copyright 2025 fabio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prjmanutencao.dal;

import java.sql.*;

/**
 * Conexão com o banco de dados.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class ModuloConexao {

    /**
     * Método responsável por estabelecer a conexão com o banco de dados.
     *
     * @return Retorna um objeto Connection se a conexão for bem-sucedida, caso
     * contrário, retorna null.
     */
    public static Connection conector() {
        java.sql.Connection conexao = null;
        // a linha a baixo chama o drive 
        String driver = "com.mysql.cj.jdbc.Driver";
        // armazenando informações referentes ao banco
        String url = "jdbc:mysql://192.168.0.50:3306/db_manutencao?characterEncoding=utf-8";
        String user = "dba";
        String password = "Dba123456@";
        // estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

}
