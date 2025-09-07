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
package com.prjmanutencao.telas;

import java.sql.*;
import com.prjmanutencao.dal.ModuloConexao;
import java.beans.PropertyVetoException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;

/**
 * Realiza a busca por ordem de serviço em aberto e encerradas e exibem em uma
 * tabela.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPesquisaros extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Construtor `TelaPesquisaros`. Responsável por inicializar a interface
     * gráfica, estabelecer conexão com o banco de dados e configurar o estado
     * inicial da aplicação, carregando informações relevantes de Ordens de
     * Serviço (OS). Este construtor é chamado automaticamente quando uma nova
     * instância da classe `TelaPesquisaros` é criada.
     *
     * Funcionalidades: - **Inicialização dos Componentes Gráficos**: -
     * `initComponents()`: Método gerado pelo ambiente de desenvolvimento
     * (NetBeans ou similar), que configura os elementos da interface como
     * botões, labels e tabelas.
     *
     * - **Estabelecimento da Conexão com o Banco de Dados**: - `conexao =
     * ModuloConexao.conector()`: Utiliza um método para conectar ao banco de
     * dados, garantindo que a aplicação possa realizar consultas e alterações
     * nos registros.
     *
     * - **Configuração Inicial das Ordens de Serviço**: - `os_aberta()`:
     * Provavelmente carrega a lista de Ordens de Serviço que ainda estão
     * abertas. - `os_fechada()`: Carrega a lista de Ordens de Serviço que já
     * foram finalizadas. - `os_abertas_quant()`: Obtém e exibe a quantidade de
     * OS abertas. - `os_encerrada_quant()`: Obtém e exibe a quantidade de OS
     * encerradas. - `alinhar()`: Ajusta visualmente a interface para garantir
     * que os elementos estejam organizados corretamente.
     *
     * Fluxo do Construtor: 1. Inicializa os componentes visuais da interface.
     * 2. Estabelece a conexão com o banco de dados. 3. Invoca métodos
     * auxiliares para carregar e organizar informações das Ordens de Serviço.
     */
    public TelaPesquisaros() {
        initComponents();
        conexao = ModuloConexao.conector();
        os_aberta();
        os_fechada();
        os_abertas_quant();
        os_encerrada_quant();
        alinhar();

    }

    /**
     * Método `alinhar`. Responsável por configurar a aparência das tabelas
     * `tblOsAberta` e `tblOsFechada`, ajustando suas larguras de colunas,
     * centralizando os cabeçalhos e garantindo que os dados fiquem bem
     * alinhados. O método também aplica uma renderização personalizada para
     * garantir que os valores dentro das células sejam exibidos corretamente.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para as colunas de `tblOsAberta` e `tblOsFechada`,
     * melhorando a organização visual dos dados.
     *
     * - **Centralização dos Cabeçalhos das Tabelas**: - Obtém os cabeçalhos
     * (`JTableHeader`) das tabelas e aplica uma renderização centralizada
     * (`SwingConstants.CENTER`).
     *
     * - **Configuração do Modo de Redimensionamento**: -
     * `setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`: Evita que as
     * colunas da tabela sejam redimensionadas automaticamente, garantindo que
     * as larguras definidas permaneçam fixas.
     *
     * - **Centralização dos Valores das Células**: - Utiliza
     * `DefaultTableCellRenderer` para garantir que todos os valores nas tabelas
     * estejam centralizados.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros e exibe uma
     * mensagem ao usuário utilizando `JOptionPane.showMessageDialog()`.
     *
     * Fluxo do Método: 1. Define as larguras das colunas de ambas as tabelas.
     * 2. Obtém os cabeçalhos das tabelas e aplica alinhamento centralizado. 3.
     * Configura a renderização das células para garantir alinhamento
     * centralizado. 4. Aplica as configurações em ambas as tabelas. 5. Trata
     * possíveis erros e exibe mensagens ao usuário.
     */
    private void alinhar() {

        int width = 80, width1 = 200, width2 = 140, width3 = 200, width4 = 90, width5 = 80, width6 = 90, width17 = 125;
        int width7 = 80, width8 = 200, width9 = 140, width10 = 200, width11 = 90, width12 = 80, width13 = 90, width14 = 125;

        JTableHeader header, header1;

        try {
            header = tblOsAberta.getTableHeader();
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblOsAberta.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblOsAberta.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblOsAberta.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblOsAberta.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblOsAberta.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblOsAberta.getColumnModel().getColumn(4).setPreferredWidth(width4);
            tblOsAberta.getColumnModel().getColumn(5).setPreferredWidth(width5);
            tblOsAberta.getColumnModel().getColumn(6).setPreferredWidth(width6);
            tblOsAberta.getColumnModel().getColumn(7).setPreferredWidth(width17);

            header1 = tblOsFechada.getTableHeader();
            DefaultTableCellRenderer centralizado1 = (DefaultTableCellRenderer) header1.getDefaultRenderer();
            centralizado1.setHorizontalAlignment(SwingConstants.CENTER);
            tblOsFechada.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblOsFechada.getColumnModel().getColumn(0).setPreferredWidth(width7);
            tblOsFechada.getColumnModel().getColumn(1).setPreferredWidth(width8);
            tblOsFechada.getColumnModel().getColumn(2).setPreferredWidth(width9);
            tblOsFechada.getColumnModel().getColumn(3).setPreferredWidth(width10);
            tblOsFechada.getColumnModel().getColumn(4).setPreferredWidth(width11);
            tblOsFechada.getColumnModel().getColumn(5).setPreferredWidth(width12);
            tblOsFechada.getColumnModel().getColumn(6).setPreferredWidth(width13);
            tblOsFechada.getColumnModel().getColumn(7).setPreferredWidth(width14);
            tblOsFechada.getColumnModel().getColumn(8).setPreferredWidth(width14);
            tblOsFechada.getColumnModel().getColumn(9).setPreferredWidth(width14);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblOsAberta.getColumnCount(); i++) {
                tblOsAberta.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
            centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblOsFechada.getColumnCount(); i++) {
                tblOsFechada.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `os_aberta`. Este método recupera as Ordens de Serviço (OS) com
     * status "Aberta" do banco de dados e exibe os resultados na tabela
     * `tblOsAberta`. Ele constrói uma query SQL com subconsultas para obter
     * detalhes dos usuários associados à OS, como o solicitante e o técnico
     * responsável. Caso ocorra um erro na execução da consulta, exibe uma
     * mensagem de erro ao usuário.
     *
     * Funcionalidades: - **Definição do Status da OS**: - O método filtra
     * apenas OS com status "Aberta".
     *
     * - **Query SQL para Recuperação de Dados**: - Recupera os seguintes campos
     * da tabela `os Os`: - `id_os`: Número da OS. - `nome_usuario`: Nome do
     * solicitante (obtido via subconsulta na tabela `usuarios`). -
     * `area_usuario`: Setor do solicitante (obtido via subconsulta). -
     * `nome_usuario`: Nome do técnico responsável (obtido via subconsulta). -
     * `tipo_os`: Tipo da OS. - `prioridade_os`: Prioridade da OS. -
     * `servico_os`: Tipo de serviço da OS. - `hora_inicial`: Horário de início
     * da OS.
     *
     * - **Execução da Consulta**: - Prepara e executa a query SQL utilizando
     * `PreparedStatement`. - Substitui o parâmetro `situacao_os = ?` pelo
     * status "Aberta".
     *
     * - **Atualização da Interface**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para transformar o resultado da consulta em um modelo de tabela. - Exibe
     * os dados na interface gráfica dentro do componente `tblOsAberta`.
     *
     * - **Tratamento de Exceções**: - Captura erros do banco de dados
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Aberta" para filtrar as OS. 2.
     * Prepara a query SQL e substitui o parâmetro necessário. 3. Executa a
     * consulta e converte o resultado para um modelo de tabela. 4. Exibe os
     * dados na interface gráfica. 5. Trata possíveis erros e exibe mensagens ao
     * usuário.
     */
    private void os_aberta() {

        String status = "Aberta";

        String sql = "select  Os.id_os as 'N° OS', (select nome_usuario from usuarios where iduser = Os.iduser1) as 'Solicitante', (select area_usuario from usuarios where iduser = Os.iduser1) as'Setor', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Técnico', Os.tipo_os as 'O.S', Os.prioridade_os as 'Prioridade', Os.servico_os as 'Serviço', Os.hora_inicial as 'Início' from os Os where situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            rs = pst.executeQuery();
            tblOsAberta.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `os_abertas_quant`. Responsável por contar e exibir a quantidade
     * de Ordens de Serviço (OS) com status "Aberta" na interface gráfica. Ele
     * consulta o banco de dados e, caso haja resultados, atualiza o campo
     * `txtOsAber` com a quantidade obtida. Caso ocorra um erro na consulta,
     * exibe uma mensagem informativa ao usuário.
     *
     * Funcionalidades: - **Definição do Status**: - Define `"Aberta"` como
     * status de filtro para contar apenas OS ativas.
     *
     * - **Consulta SQL para Contagem de Registros**: - Usa `SELECT COUNT(*)
     * FROM os WHERE situacao_os = ?` para calcular a quantidade de OS abertas.
     * - Utiliza `PreparedStatement` para evitar injeção de SQL e otimizar a
     * consulta.
     *
     * - **Processamento do Resultado**: - Executa a query e, caso haja
     * resultado (`rs.next()`), define o valor obtido no campo `txtOsAber`.
     *
     * - **Tratamento de Exceções**: - Captura erros de banco de dados
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Aberta" como critério de filtragem.
     * 2. Prepara e executa a consulta SQL de contagem. 3. Caso haja resultado,
     * exibe a quantidade de OS abertas na interface. 4. Caso ocorra erro, exibe
     * uma mensagem ao usuário.
     */
    private void os_abertas_quant() {

        String status = "Aberta";

        String sql = "select count(*) from os  where situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOsAber.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisar_os`. Este método realiza uma consulta ao banco de dados
     * para buscar Ordens de Serviço (OS) com status "Aberta". Ele permite que o
     * usuário pesquise OS abertas com base no nome do solicitante ou no número
     * da OS. Os resultados são exibidos na tabela `tblOsAberta` e organizados
     * visualmente pelo método `alinhar()`.
     *
     * Funcionalidades: - **Definição do Status**: - Define o status "Aberta"
     * como critério principal para filtrar as OS.
     *
     * - **Montagem da Query SQL**: - Consulta na tabela `os`, incluindo
     * subconsultas para recuperar informações detalhadas sobre os usuários: -
     * Nome do solicitante e setor associados à OS. - Nome do técnico
     * responsável pela OS. - O `INNER JOIN` associa a tabela `os` com a tabela
     * `usuarios`, facilitando buscas relacionadas ao solicitante. - A condição
     * `WHERE` permite filtrar as OS por: - Nome do solicitante
     * (`Usu.nome_usuario LIKE ?`). - Número da OS (`Os.id_os LIKE ?`). - A
     * combinação de condições com `OR` oferece maior flexibilidade na pesquisa.
     * - O `ORDER BY Os.id_os ASC` organiza os resultados em ordem crescente de
     * número da OS.
     *
     * - **Execução da Consulta**: - Usa `PreparedStatement` para evitar
     * vulnerabilidades e melhorar o desempenho. - Substitui os parâmetros `?`
     * com: - `status`: Define "Aberta" como o status da OS. -
     * `txtOsAbr.getText() + "%"`, permitindo busca parcial pelo nome do
     * solicitante ou número da OS.
     *
     * - **Atualização da Interface**: - Converte os resultados da consulta em
     * um modelo de tabela com `DbUtils.resultSetToTableModel(rs)`. - Organiza
     * os dados da tabela visualmente com `alinhar()`.
     *
     * - **Tratamento de Exceções**: - Captura falhas na execução da consulta
     * SQL (`SQLException`) e exibe uma mensagem ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Aberta" como filtro principal. 2.
     * Prepara e executa a consulta SQL, filtrando por nome do solicitante ou
     * número da OS. 3. Exibe os resultados na tabela `tblOsAberta`. 4. Ajusta a
     * exibição com o método `alinhar()`. 5. Captura e trata possíveis erros,
     * exibindo mensagens informativas.
     */
    private void pesquisar_os() {

        String status = "Aberta";

        String sql = "select  Os.id_os as 'N° OS', (select nome_usuario from usuarios where iduser = Os.iduser1) as 'Solicitante', (select area_usuario from usuarios where iduser = Os.iduser1) as'Setor', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Técnico', Os.tipo_os as 'O.S', Os.prioridade_os as 'Prioridade', Os.servico_os as 'Serviço', Os.desc_os as 'Descrição', Os.hora_inicial as 'Início' from os Os inner join usuarios Usu on Os.iduser1 = Usu.iduser where Os.situacao_os = ? and Usu.nome_usuario like ? or Os.situacao_os = ? and Os.id_os like ? order by Os.id_os asc";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            pst.setString(2, txtOsAbr.getText() + "%");
            pst.setString(3, status);
            pst.setString(4, txtOsAbr.getText() + "%");
            rs = pst.executeQuery();
            tblOsAberta.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `os_fechada`. Este método recupera as Ordens de Serviço (OS) com
     * status "Encerrada" do banco de dados e exibe os resultados na tabela
     * `tblOsFechada`. Ele constrói uma query SQL com subconsultas para obter
     * detalhes dos usuários associados à OS, como o solicitante e o técnico
     * responsável. Caso ocorra um erro na execução da consulta, exibe uma
     * mensagem de erro ao usuário.
     *
     * Funcionalidades: - **Definição do Status da OS**: - Filtra apenas OS com
     * status "Encerrada".
     *
     * - **Query SQL para Recuperação de Dados**: - Recupera os seguintes campos
     * da tabela `os`: - `id_os`: Número da OS. - `nome_usuario`: Nome do
     * solicitante (obtido via subconsulta na tabela `usuarios`). -
     * `area_usuario`: Setor do solicitante (obtido via subconsulta). -
     * `nome_usuario`: Nome do técnico responsável (obtido via subconsulta). -
     * `tipo_os`: Tipo da OS. - `prioridade_os`: Prioridade da OS. -
     * `servico_os`: Tipo de serviço da OS. - `hora_inicial`: Horário de início
     * da OS. - `hora_final`: Horário de término da OS. - `cro_os`: Detalhes
     * sobre o atendimento realizado.
     *
     * - **Execução da Consulta**: - Prepara e executa a query SQL utilizando
     * `PreparedStatement`. - Substitui o parâmetro `situacao_os = ?` pelo
     * status "Encerrada".
     *
     * - **Atualização da Interface**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para transformar o resultado da consulta em um modelo de tabela. - Exibe
     * os dados na interface gráfica dentro do componente `tblOsFechada`.
     *
     * - **Tratamento de Exceções**: - Captura erros do banco de dados
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Encerrada" para filtrar as OS. 2.
     * Prepara a query SQL e substitui o parâmetro necessário. 3. Executa a
     * consulta e converte o resultado para um modelo de tabela. 4. Exibe os
     * dados na interface gráfica. 5. Trata possíveis erros e exibe mensagens ao
     * usuário.
     */
    private void os_fechada() {

        String status = "Encerrada";

        String sql = "select  Os.id_os as 'N° OS', (select nome_usuario from usuarios where iduser = Os.iduser1) as 'Solicitante', (select area_usuario from usuarios where iduser = Os.iduser1) as'Setor', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Técnico', Os.tipo_os as 'O.S', Os.prioridade_os as 'Prioridade', Os.servico_os as 'Serviço', Os.hora_inicial as 'Início', Os.hora_final as 'Término', Os.cro_os 'Atendimento' from os Os where situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            rs = pst.executeQuery();
            tblOsFechada.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `os_encerrada_quant`. Responsável por contar e exibir a quantidade
     * de Ordens de Serviço (OS) com status "Encerrada" na interface gráfica.
     * Ele consulta o banco de dados e, caso haja resultados, atualiza o campo
     * `txtOsEnce` com a quantidade obtida. Caso ocorra um erro na consulta,
     * exibe uma mensagem informativa ao usuário.
     *
     * Funcionalidades: - **Definição do Status**: - Define `"Encerrada"` como
     * status de filtro para contar apenas OS concluídas.
     *
     * - **Consulta SQL para Contagem de Registros**: - Usa `SELECT COUNT(*)
     * FROM os WHERE situacao_os = ?` para calcular a quantidade de OS
     * encerradas. - Utiliza `PreparedStatement` para evitar injeção de SQL e
     * otimizar a consulta.
     *
     * - **Processamento do Resultado**: - Executa a query e, caso haja
     * resultado (`rs.next()`), define o valor obtido no campo `txtOsEnce`.
     *
     * - **Tratamento de Exceções**: - Captura erros de banco de dados
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Encerrada" como critério de
     * filtragem. 2. Prepara e executa a consulta SQL de contagem. 3. Caso haja
     * resultado, exibe a quantidade de OS encerradas na interface. 4. Caso
     * ocorra erro, exibe uma mensagem ao usuário.
     */
    private void os_encerrada_quant() {

        String status = "Encerrada";

        String sql = "select count(*) from os  where situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOsEnce.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisar_os1`. Este método realiza uma consulta ao banco de
     * dados para buscar Ordens de Serviço (OS) com status "Encerrada". Ele
     * permite que o usuário pesquise por OS com base no nome do solicitante ou
     * no número da OS. Os resultados são exibidos na tabela `tblOsFechada` e
     * organizados visualmente pelo método `alinhar()`.
     *
     * Funcionalidades: - **Definição do Status**: - Define o status "Encerrada"
     * para filtrar as OS.
     *
     * - **Montagem da Query SQL**: - Utiliza uma consulta SQL para: - Buscar OS
     * encerradas pelo nome do solicitante (`Usu.nome_usuario LIKE ?`). - Buscar
     * OS encerradas pelo número da OS (`Os.id_os LIKE ?`). - As condições são
     * combinadas com `OR`, permitindo flexibilidade na pesquisa. - Usa `INNER
     * JOIN` para associar a tabela `os` com a tabela `usuarios`.
     *
     * - **Execução da Consulta**: - Utiliza `PreparedStatement` para evitar
     * vulnerabilidades e melhorar o desempenho. - Substitui os parâmetros `?`
     * pelos valores: - `status`: Define "Encerrada" como status das OS. -
     * `txtOsFec.getText() + "%"`: Permite busca parcial tanto no nome quanto no
     * número da OS.
     *
     * - **Atualização da Interface**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados da consulta em um modelo de tabela. - Invoca
     * o método `alinhar()` para organizar visualmente os dados exibidos na
     * tabela.
     *
     * - **Tratamento de Exceções**: - Captura possíveis falhas na execução da
     * consulta SQL e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Encerrada" como critério de
     * filtragem. 2. Prepara e executa a consulta SQL com base no nome do
     * solicitante ou no número da OS. 3. Exibe os resultados na tabela
     * `tblOsFechada`. 4. Ajusta a visualização dos dados com `alinhar()`. 5.
     * Captura e trata possíveis erros.
     */
    private void pesquisar_os1() {

        String status = "Encerrada";

        String sql = "select Os.id_os as 'N° OS', (select nome_usuario from usuarios where iduser = Os.iduser1) as 'Solicitante', (select area_usuario from usuarios where iduser = Os.iduser1) as'Setor',(select nome_usuario from usuarios Usu where iduser = Os.iduser) as 'Técnico', Os.tipo_os as 'O.S', Os.prioridade_os as 'Prioridade', Os.servico_os as 'Serviço', Os.hora_inicial as 'Início', Os.hora_final as 'Término', Os.cro_os 'Atendimento' from os Os inner join usuarios Usu on Os.iduser1 = Usu.iduser where Os.situacao_os = ? and Usu.nome_usuario like ? or Os.situacao_os = ? and Os.id_os like ? order by Os.id_os asc ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, status);
            pst.setString(2, txtOsFec.getText() + "%");
            pst.setString(3, status);
            pst.setString(4, txtOsFec.getText() + "%");
            rs = pst.executeQuery();
            tblOsFechada.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtOsAbr = new javax.swing.JTextField();
        txtOsFec = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOsAberta = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOsFechada = new javax.swing.JTable();
        btnAtualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtOsAber = new javax.swing.JTextField();
        txtOsEnce = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Lista O.S");
        setFocusCycleRoot(false);
        setPreferredSize(new java.awt.Dimension(1007, 672));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        txtOsAbr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOsAbr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOsAbrKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsAbrKeyTyped(evt);
            }
        });
        getContentPane().add(txtOsAbr);
        txtOsAbr.setBounds(279, 145, 395, 26);

        txtOsFec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOsFec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOsFecKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsFecKeyTyped(evt);
            }
        });
        getContentPane().add(txtOsFec);
        txtOsFec.setBounds(279, 373, 395, 26);

        tblOsAberta = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblOsAberta.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblOsAberta.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        tblOsAberta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N° O.S", "Solicitante ", "Setor", "Técnico ", "O.S", "Serviço", "Descrisção", "Início"
            }
        ));
        tblOsAberta.getTableHeader().setReorderingAllowed(false);
        tblOsAberta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsAbertaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOsAberta);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(30, 198, 937, 112);

        tblOsFechada = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblOsFechada.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblOsFechada.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        tblOsFechada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N° O.S", "Solicitante", "Setor", "Técnico", "O.S", "Serviço", "Descrisção", "Inínicio", "Término", "Atend."
            }
        ));
        tblOsFechada.getTableHeader().setReorderingAllowed(false);
        tblOsFechada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsFechadaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblOsFechada);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(30, 427, 937, 112);

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/1216559_arrows_refresh_reload_repeat_rotate_icon.png"))); // NOI18N
        btnAtualizar.setToolTipText("Atualizar Listas");
        btnAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAtualizar);
        btnAtualizar.setBounds(585, 561, 64, 64);

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("O.S Abertas :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(668, 564, 110, 26);

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("O.S Encerradas :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(660, 594, 110, 26);

        txtOsAber.setEditable(false);
        txtOsAber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtOsAber);
        txtOsAber.setBounds(772, 562, 60, 28);

        txtOsEnce.setEditable(false);
        txtOsEnce.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtOsEnce);
        txtOsEnce.setBounds(772, 594, 60, 28);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Lista Os.png"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(1007, 672));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(-6, -20, 1007, 672);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnAtualizarActionPerformed`. Este método é acionado quando o
     * botão de atualização da interface gráfica é pressionado. Ele executa uma
     * série de chamadas de métodos que recarregam as informações da tela,
     * garantindo que os dados sobre as Ordens de Serviço (OS) estejam
     * atualizados.
     *
     * Funcionalidades: - **Atualização das OS**: - `os_aberta()`: Atualiza a
     * lista de OS abertas. - `os_fechada()`: Atualiza a lista de OS fechadas. -
     * `os_abertas_quant()`: Atualiza o contador de OS abertas. -
     * `os_encerrada_quant()`: Atualiza o contador de OS encerradas.
     *
     * - **Ajuste da Interface Gráfica**: - `alinhar()`: Ajusta a apresentação
     * dos dados nas tabelas, garantindo uma exibição bem organizada.
     *
     * Fluxo do Método: 1. O usuário pressiona o botão de atualização. 2. Os
     * métodos são invocados para atualizar os dados e contadores das OS. 3. A
     * interface é ajustada para refletir os dados atualizados.
     */
    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed

        os_aberta();
        os_fechada();
        os_abertas_quant();
        os_encerrada_quant();
        txtOsAbr.setText(null);
        txtOsFec.setText(null);
        alinhar();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    /**
     * Método `txtOsAbrKeyReleased`. Este método é acionado automaticamente
     * quando uma tecla é liberada dentro do campo de texto `txtOsAbr`. Sempre
     * que o usuário digita algo nesse campo e solta a tecla, ele chama o método
     * `pesquisar_os()` para realizar uma busca dinâmica das Ordens de Serviço
     * (OS) abertas.
     *
     * Funcionalidades: - **Detecção de Evento**: - `KeyReleased`: O evento é
     * disparado quando o usuário solta uma tecla após digitar no campo de
     * pesquisa.
     *
     * - **Pesquisa de OS em Tempo Real**: - Chama `pesquisar_os()` para buscar
     * OS com base no texto digitado, permitindo que os resultados sejam
     * atualizados conforme o usuário insere informações.
     *
     * Fluxo do Método: 1. O usuário digita no campo `txtOsAbr`. 2. Quando uma
     * tecla é liberada, o evento `KeyReleased` é disparado. 3. O método
     * `pesquisar_os()` é chamado, atualizando a lista de OS abertas na
     * interface.
     */
    private void txtOsAbrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsAbrKeyReleased

        pesquisar_os();
    }//GEN-LAST:event_txtOsAbrKeyReleased

    /**
     * Método `tblOsAbertaMouseClicked`. Este método é acionado quando o usuário
     * clica em uma linha da tabela `tblOsAberta`. Ele abre uma nova instância
     * da janela `TelaOs1`, configura seu estado inicial e maximiza a exibição.
     * Caso ocorra um erro ao tentar maximizar a janela, exibe uma mensagem ao
     * usuário. Após abrir a nova janela, fecha a janela atual para evitar
     * múltiplas instâncias abertas.
     *
     * Funcionalidades: - **Criação e Exibição da Tela**: - `TelaOs1 os = new
     * TelaOs1()`: Cria uma nova instância da tela `TelaOs1`. -
     * `os.setVisible(true)`: Torna a janela visível na interface gráfica. -
     * `getParent().add(os)`: Adiciona a nova tela ao contêiner pai.
     *
     * - **Configuração da OS na Nova Tela**: - `os.setOsInicial()`:
     * Provavelmente configura os dados iniciais da OS na nova janela.
     *
     * - **Maximização da Janela**: - `os.setMaximum(true)`: Tenta maximizar a
     * tela para melhor visualização. - Caso ocorra erro
     * (`PropertyVetoException`), exibe uma mensagem ao usuário.
     *
     * - **Encerramento da Janela Atual**: - `dispose()`: Fecha a janela atual
     * para evitar duplicações ou múltiplas instâncias abertas.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela `tblOsAberta`.
     * 2. Cria e exibe uma nova instância da janela `TelaOs1`. 3. Configura os
     * dados da OS na nova tela. 4. Maximiza a nova janela (caso possível). 5.
     * Fecha a janela atual para evitar múltiplas instâncias.
     */
    private void tblOsAbertaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsAbertaMouseClicked

        TelaOs1 os = new TelaOs1();
        os.setVisible(true);
        getParent().add(os);
        os.setOsInicial();
        try {
            os.setMaximum(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        dispose();
    }//GEN-LAST:event_tblOsAbertaMouseClicked

    /**
     * Método `txtOsFecKeyReleased`. Este método é acionado automaticamente
     * quando uma tecla é liberada dentro do campo de texto `txtOsFec`. Sempre
     * que o usuário digita algo nesse campo e solta a tecla, ele chama o método
     * `pesquisar_os1()` para realizar uma busca dinâmica das Ordens de Serviço
     * (OS) encerradas.
     *
     * Funcionalidades: - **Detecção de Evento**: - `KeyReleased`: O evento é
     * disparado quando o usuário solta uma tecla após digitar no campo de
     * pesquisa.
     *
     * - **Pesquisa de OS em Tempo Real**: - Chama `pesquisar_os1()` para buscar
     * OS com base no texto digitado, permitindo que os resultados sejam
     * atualizados conforme o usuário insere informações.
     *
     * Fluxo do Método: 1. O usuário digita no campo `txtOsFec`. 2. Quando uma
     * tecla é liberada, o evento `KeyReleased` é disparado. 3. O método
     * `pesquisar_os1()` é chamado, atualizando a lista de OS encerradas na
     * interface.
     */
    private void txtOsFecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsFecKeyReleased

        pesquisar_os1();
    }//GEN-LAST:event_txtOsFecKeyReleased

    /**
     * Método `tblOsFechadaMouseClicked`. Este método é acionado quando o
     * usuário clica em uma linha da tabela `tblOsFechada`. Ele abre uma nova
     * instância da janela `TelaOs1`, configura seu estado inicial para
     * representar uma OS finalizada, e maximiza a exibição. Caso ocorra um erro
     * ao tentar maximizar a janela, exibe uma mensagem ao usuário. Após abrir a
     * nova janela, fecha a janela atual para evitar múltiplas instâncias
     * abertas.
     *
     * Funcionalidades: - **Criação e Exibição da Tela**: - `TelaOs1 os = new
     * TelaOs1()`: Cria uma nova instância da tela `TelaOs1`. -
     * `os.setVisible(true)`: Torna a janela visível na interface gráfica. -
     * `getParent().add(os)`: Adiciona a nova tela ao contêiner pai.
     *
     * - **Configuração da OS na Nova Tela**: - `os.setOsFinal()`: Configura os
     * dados para representar uma OS finalizada.
     *
     * - **Maximização da Janela**: - `os.setMaximum(true)`: Tenta maximizar a
     * tela para melhor visualização. - Caso ocorra erro
     * (`PropertyVetoException`), exibe uma mensagem ao usuário.
     *
     * - **Encerramento da Janela Atual**: - `dispose()`: Fecha a janela atual
     * para evitar duplicações ou múltiplas instâncias abertas.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela
     * `tblOsFechada`. 2. Cria e exibe uma nova instância da janela `TelaOs1`.
     * 3. Configura os dados da OS finalizada na nova tela. 4. Maximiza a nova
     * janela (caso possível). 5. Fecha a janela atual para evitar múltiplas
     * instâncias.
     */
    private void tblOsFechadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsFechadaMouseClicked

        TelaOs1 os = new TelaOs1();
        os.setVisible(true);
        getParent().add(os);
        try {
            os.setMaximum(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        os.setOsFinal();
        dispose();
    }//GEN-LAST:event_tblOsFechadaMouseClicked

    /**
     * Método `txtOsAbrKeyTyped`. Este método é acionado automaticamente toda
     * vez que o usuário digita um caractere no campo de texto `txtOsAbr`. Ele
     * verifica cada caractere inserido, permitindo apenas letras (maiúsculas ou
     * minúsculas), números e caracteres especiais definidos. Qualquer caractere
     * que não esteja na lista de permitidos é bloqueado e não será exibido no
     * campo de texto.
     *
     * Funcionalidades: - **Controle de Entrada**: - Define uma lista de
     * caracteres válidos na variável `String caracter`. - Bloqueia a inclusão
     * de caracteres inválidos usando `evt.consume()`, removendo o evento do
     * processamento.
     *
     * - **Feedback ao Usuário** (Opcional): - A linha comentada
     * `Toolkit.getDefaultToolkit().beep()` pode ser usada para emitir um som
     * quando caracteres inválidos forem digitados.
     *
     * Fluxo do Método: 1. O usuário digita um caractere no campo `txtOsAbr`. 2.
     * O evento `KeyTyped` verifica o caractere digitado. 3. Se o caractere não
     * estiver na lista de permitidos, o evento é consumido e o caractere não
     * aparece no campo de texto. 4. Opcionalmente, pode emitir um som para
     * informar ao usuário que o caractere digitado não é válido.
     */
    private void txtOsAbrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsAbrKeyTyped
        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtOsAbrKeyTyped

    /**
     * Método `txtOsFecKeyTyped`. Este método é acionado automaticamente toda
     * vez que o usuário digita um caractere no campo de texto `txtOsFec`. Ele
     * verifica os caracteres inseridos, permitindo apenas letras (maiúsculas ou
     * minúsculas), números e um espaço. Caso o caractere digitado não esteja na
     * lista de permitidos, o evento é consumido, impedindo sua inserção no
     * campo de texto.
     *
     * Funcionalidades: - **Detecção de Evento**: - O evento `KeyTyped` é
     * disparado quando o usuário digita um caractere no campo de texto.
     *
     * - **Filtro de Caracteres Permitidos**: - Define uma lista de caracteres
     * válidos na variável `String caracter`. - Verifica se o caractere digitado
     * está na lista. Caso contrário, consome o evento usando `evt.consume()`.
     *
     * - **Feedback ao Usuário**: - A linha comentada
     * `Toolkit.getDefaultToolkit().beep()` pode ser ativada para emitir um som
     * ao digitar caracteres inválidos, informando o erro.
     *
     * Fluxo do Método: 1. O usuário digita um caractere no campo de texto
     * `txtOsFec`. 2. O evento `KeyTyped` verifica se o caractere digitado é
     * permitido. 3. Caso o caractere seja inválido, o evento é consumido,
     * impedindo sua inclusão. 4. Opcionalmente, pode emitir um som para
     * sinalizar o erro ao usuário.
     */
    private void txtOsFecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsFecKeyTyped
        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtOsFecKeyTyped

    /**
     * Método `formInternalFrameOpened`. Este método é acionado automaticamente
     * quando a janela interna (`JInternalFrame`) é aberta. Ele configura as
     * propriedades visuais do botão `btnAtualizar`, ajustando sua aparência
     * para se integrar melhor ao design da interface gráfica.
     *
     * Funcionalidades: - **Alteração do Estilo do Botão**: -
     * `btnAtualizar.setBorderPainted(false)`: Remove a borda do botão,
     * tornando-o mais limpo visualmente. -
     * `btnAtualizar.setContentAreaFilled(false)`: Remove o preenchimento de
     * fundo do botão. - `btnAtualizar.setOpaque(false)`: Define o botão como
     * transparente, permitindo que o fundo da janela apareça.
     *
     * Fluxo do Método: 1. O evento `InternalFrameOpened` é disparado
     * automaticamente quando a janela interna é aberta. 2. As propriedades
     * visuais do botão `btnAtualizar` são ajustadas para remover bordas,
     * preenchimento e opacidade.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setContentAreaFilled(false);
        btnAtualizar.setOpaque(false);
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblOsAberta;
    public static javax.swing.JTable tblOsFechada;
    private javax.swing.JTextField txtOsAber;
    private javax.swing.JTextField txtOsAbr;
    private javax.swing.JTextField txtOsEnce;
    private javax.swing.JTextField txtOsFec;
    // End of variables declaration//GEN-END:variables
}
