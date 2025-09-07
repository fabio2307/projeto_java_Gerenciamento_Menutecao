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

import com.prjmanutencao.dal.ModuloConexao;
import static com.prjmanutencao.telas.TelaLogin.id;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;

/**
 * Mostra as ordem de serviço em aberto por setor ou no total e mostra as que
 * foram abertas pelo o usuário logado.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaChamados extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String chamado;
    String setor;
    String comp;

    /**
     * Construtor `TelaChamados`. Este método inicializa a interface da tela de
     * chamados, estabelecendo a conexão com o banco de dados, configurando
     * variáveis de controle e ajustando elementos visuais para exibição
     * adequada dos chamados. Ele é chamado automaticamente ao criar uma nova
     * instância da classe `TelaChamados`.
     *
     * Funcionalidades: - **Inicialização de Componentes Gráficos**: -
     * `initComponents()`: Método gerado pelo ambiente de desenvolvimento que
     * configura os elementos da interface, como botões, tabelas e caixas de
     * texto.
     *
     * - **Estabelecimento da Conexão com o Banco de Dados**: - `conexao =
     * ModuloConexao.conector()`: Utiliza um método para conectar ao banco de
     * dados, garantindo que os chamados possam ser manipulados e consultados.
     *
     * - **Configuração Inicial**: - `setor = "Todos"`: Define o setor inicial
     * como "Todos". - `chamado = "todos"`: Define o tipo inicial de chamados
     * como "todos".
     *
     * - **Configuração de Botões**: - `rbtSetGeral.setSelected(true)`: Define o
     * botão de seleção geral como ativo. - `rbtSetGeral.setForeground(new
     * Color(39, 44, 182))`: Altera a cor do texto do botão para azul escuro.
     *
     * - **Estilização da Tabela**: -
     * `tblChamados.setDefaultRenderer(Object.class, new TabelaColor1())`:
     * Aplica um renderizador personalizado para estilizar os dados da tabela
     * `tblChamados`.
     *
     * - **Configuração do ScrollPane**: -
     * `jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS)`:
     * Configura a barra de rolagem vertical para estar sempre visível,
     * garantindo acessibilidade na tabela.
     *
     * Fluxo do Método: 1. Inicializa os componentes visuais da tela. 2.
     * Estabelece a conexão com o banco de dados. 3. Configura variáveis de
     * controle para definir o estado inicial da tela. 4. Ajusta o estilo visual
     * da interface, incluindo botões e tabela.
     */
    public TelaChamados() {
        initComponents();
        conexao = ModuloConexao.conector();
        setor = "Todos";
        chamado = "todos";

        rbtSetGeral.setSelected(true);
        rbtSetGeral.setForeground(new Color(39, 44, 182));

        tblChamados.setDefaultRenderer(Object.class, new TabelaColor1());
        jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Classe `JlabelGradient`. Estende a classe `JLabel` para adicionar um
     * efeito visual de gradiente personalizado no fundo do componente. A classe
     * sobrescreve o método `paintComponent(Graphics estilo)` para realizar a
     * renderização do gradiente.
     *
     * Funcionalidades: - **Renderização Personalizada**: - Sobrescreve o método
     * `paintComponent` para desenhar o fundo do componente com um gradiente. -
     * Utiliza as classes `Graphics2D` e `GradientPaint` para criar um efeito
     * visual suave.
     *
     * - **Definição de Cores**: - Define duas cores principais para o
     * gradiente: - `cor1`: Cor inicial do gradiente (azul claro, com opacidade
     * ajustada). - `cor2`: Cor final do gradiente (branco).
     *
     * - **Criação do Gradiente**: - `GradientPaint gp = new GradientPaint(0, 0,
     * cor1, 0, height, cor2)`: Cria um gradiente que varia verticalmente, indo
     * de `cor1` no topo até `cor2` na base.
     *
     * - **Preenchimento do Componente**: - `g2d.fillRect(0, 0, width, height)`:
     * Preenche todo o espaço do componente com o gradiente.
     *
     * Fluxo do Método: 1. Obtém as dimensões do componente (`getWidth()` e
     * `getHeight()`). 2. Define as cores e cria o gradiente. 3. Configura o
     * gradiente como tinta para o `Graphics2D`. 4. Preenche o fundo do
     * componente com o gradiente.
     */
    class JlabelGradient extends JLabel {

        @Override
        protected void paintComponent(Graphics estilo) {

            Graphics2D g2d = (Graphics2D) estilo;
            int width = getWidth();
            int height = getHeight();
            Color cor1 = new Color(109, 186, 255, 80);
            Color cor2 = new Color(255, 255, 255);
            GradientPaint gp = new GradientPaint(0, 0, cor1, 0, height, cor2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);

        }
    }

    /**
     * Classe `TabelaColor1`. Estende `DefaultTableCellRenderer` para
     * implementar uma renderização personalizada das células de uma tabela
     * (`JTable`). Permite modificar a aparência das células com base nos dados,
     * como cores de fundo e texto, adicionando destaques visuais de acordo com
     * as condições especificadas.
     *
     * Funcionalidades: - **Renderização Personalizada de Células**: -
     * Sobrescreve o método `getTableCellRendererComponent` para definir cores
     * de fundo e texto das células com base em valores específicos contidos na
     * tabela.
     *
     * - **Destaque de Linhas com Base em Condições**: - Verifica se o valor de
     * `texto` (obtido da coluna 1) corresponde ao valor definido por `id`: - Se
     * a condição for atendida, o texto da célula é azul (`Color.BLUE`) e o
     * fundo é branco. - Personaliza o fundo das células na coluna 4 (`column ==
     * 4`) com base no valor de `texto1`: - `"Baixa"`: Fundo verde translúcido.
     * - `"Média"`: Fundo amarelo translúcido. - `"Alta"`: Fundo vermelho
     * translúcido.
     *
     * - **Alinhamento Horizontal do Texto**: - Todas as células têm o conteúdo
     * centralizado horizontalmente na coluna.
     *
     * - **Renderização por Padrão**: - Caso nenhuma condição seja atendida, a
     * célula mantém as cores padrão: texto preto e fundo branco.
     *
     * Fluxo do Método: 1. Obtém os valores das células nas colunas 1 e 4 para
     * comparação (`texto` e `texto1`). 2. Aplica cores ao texto e ao fundo com
     * base nas condições especificadas. 3. Retorna o componente `JLabel`
     * configurado.
     */
    public class TabelaColor1 extends DefaultTableCellRenderer {

        public TabelaColor1() {

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color c;
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c = Color.BLACK;
            Object texto = table.getValueAt(row, 1);
            Object texto1 = table.getValueAt(row, 4);
            setHorizontalAlignment(CENTER);

            if (texto != null && id.equals(texto.toString())) {
                label.setForeground(Color.BLUE);
                label.setBackground(java.awt.Color.WHITE);
//                label.setBackground(new Color(63, 208, 255, 90));
//                for (int i = 0; i < tblChamados.getColumnCount(); i++) {

//                tblChamados.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//            }
                if (texto1 != null && texto1.equals("Baixa") && column == 4) {
                    label.setBackground(new Color(0, 255, 0, 95));
                }
                if (texto1 != null && texto1.equals("Média") && column == 4) {
                    label.setBackground(new Color(255, 255, 0, 95));
                }
                if (texto1 != null && texto1.equals("Alta") && column == 4) {
                    label.setBackground(new Color(255, 0, 0, 95));
                }

            } else {
                label.setForeground(java.awt.Color.BLACK);
                label.setBackground(java.awt.Color.WHITE);
                if (texto1 != null && texto1.equals("Baixa") && column == 4) {
                    label.setBackground(new Color(0, 255, 0, 95));
                }
                if (texto1 != null && texto1.equals("Média") && column == 4) {
                    label.setBackground(new Color(255, 255, 0, 95));
                }
                if (texto1 != null && texto1.equals("Alta") && column == 4) {
                    label.setBackground(new Color(255, 0, 0, 95));
                }
            }

            return label;
        }
    }

    /**
     * Método `alinhamentoTabela`. Este método configura a aparência da tabela
     * `tblChamados`, ajustando larguras das colunas, estilizando os cabeçalhos
     * e definindo a altura das linhas para melhorar a exibição dos dados. Caso
     * ocorra algum erro durante o processo, exibe uma mensagem ao usuário
     * informando o problema.
     *
     * Funcionalidades: - **Configuração de Larguras das Colunas**: - Define
     * valores específicos de largura para cada coluna da tabela (`width`,
     * `width1`, etc.), garantindo uma distribuição adequada dos dados.
     *
     * - **Estilização dos Cabeçalhos**: - Modifica a aparência dos cabeçalhos
     * da tabela com as seguintes configurações: - `header.setPreferredSize(new
     * Dimension(0, 30))`: Define a altura do cabeçalho. - `header.setFont(new
     * Font("Arial", Font.BOLD, 15))`: Define a fonte e estilo do texto. -
     * `header.setForeground(new Color(0, 0, 51))`: Define a cor do texto do
     * cabeçalho (azul escuro). - Centraliza o conteúdo do cabeçalho com
     * `centralizado.setHorizontalAlignment(SwingConstants.CENTER)`.
     *
     * - **Ajuste do Redimensionamento**: -
     * `tblChamados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`:
     * Desativa o redimensionamento automático das colunas, garantindo que os
     * valores de largura permaneçam fixos.
     *
     * - **Altura das Linhas**: - `tblChamados.setRowHeight(22)`: Define a
     * altura das linhas para melhorar a legibilidade.
     *
     * - **Tratamento de Exceções**: - Captura erros durante o processo e exibe
     * uma mensagem ao usuário utilizando `JOptionPane.showMessageDialog`.
     *
     * Fluxo do Método: 1. Obtém o cabeçalho da tabela (`getTableHeader()`). 2.
     * Configura o estilo do cabeçalho (altura, fonte, cor e alinhamento). 3.
     * Define as larguras das colunas e ajusta a altura das linhas. 4. Caso
     * ocorra algum erro, exibe uma mensagem ao usuário.
     */
    private void alinhamentoTabela() {

        int width = 80, width1 = 80, width2 = 200, width3 = 190, width4 = 120, width5 = 199;
        JTableHeader header;

        try {
            header = tblChamados.getTableHeader();
            header.setPreferredSize(new Dimension(0, 30));
            header.setFont(new Font("Arial", Font.BOLD, 15));
            header.setForeground(new Color(0, 0, 51));
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblChamados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblChamados.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblChamados.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblChamados.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblChamados.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblChamados.getColumnModel().getColumn(4).setPreferredWidth(width4);
            tblChamados.getColumnModel().getColumn(5).setPreferredWidth(width5);
            tblChamados.setRowHeight(22);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `osSetores`. Este método realiza a exibição de Ordens de Serviço
     * (OS) com status "Aberta" para diferentes setores. Dependendo do setor
     * selecionado na variável `setor`, ele monta e executa uma consulta SQL,
     * exibe os resultados na tabela `tblChamados` e ajusta sua aparência
     * utilizando o método `alinhamentoTabela()`. Além disso, personaliza a
     * aparência de certos elementos visuais, como `lblGeral`.
     *
     * Funcionalidades: - **Alteração Visual**: - Atualiza as cores de texto dos
     * rótulos `lblAtender`, `lblChamadosAbertos` e `lblGeral`. - Modifica o
     * estilo do texto do rótulo `lblGeral` para destacá-lo no setor atual.
     *
     * - **Filtragem por Setor**: - Utiliza um `switch` para determinar o SQL
     * adequado a ser executado com base no setor selecionado. - Cada caso monta
     * uma consulta SQL para filtrar as OS com `situacao_os = "Aberta"` e
     * `area_usuario = setor`.
     *
     * - **Execução da Consulta SQL**: - Usa `PreparedStatement` para executar a
     * consulta de forma segura e eficiente. - Os resultados são atribuídos à
     * tabela `tblChamados` utilizando `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Configuração da Tabela**: - Chama `alinhamentoTabela()` para ajustar
     * larguras de colunas, alinhamento e altura das linhas na tabela.
     *
     * - **Encerramento da Conexão**: - Fecha a conexão com o banco de dados
     * após a execução da consulta, garantindo boa prática de gerenciamento de
     * recursos.
     *
     * - **Tratamento de Exceções**: - Captura erros de `SQLException` e exibe
     * mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Atualiza a aparência dos rótulos. 2. Identifica o
     * setor selecionado e executa a consulta SQL apropriada. 3. Exibe os
     * resultados na tabela e ajusta sua aparência. 4. Fecha a conexão com o
     * banco de dados. 5. Em caso de erro, exibe uma mensagem ao usuário.
     */
    private void osSetores() {

        lblAtender.setForeground(new Color(0, 0, 51));
        lblChamadosAbertos.setForeground(new Color(0, 0, 51));
        lblGeral.setForeground(new Color(39, 44, 182));

        String status = "Aberta";

        switch (setor) {

            case "A e B (Alimentos e Bebidas)":
                String sql1 = "select Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql1);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 11));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Administrativo":
                String sql2 = "select Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql2);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Eventos":
                String sql3 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql3);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Governança":
                String sql4 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql4);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Manutenção":
                String sql5 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql5);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Mordomia":
                String sql6 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql6);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Recepção":
                String sql7 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql7);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    lblGeral.setFont(new Font("Tahoma", Font.BOLD, 12));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            case "Segurança":
                String sql8 = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ? and area_usuario = ?";
                try {
                    pst = conexao.prepareStatement(sql8);
                    pst.setString(1, status);
                    pst.setString(2, setor);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;
            default:
                String sql = "select  Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where situacao_os = ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, status);
                    rs = pst.executeQuery();
                    tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
                    alinhamentoTabela();
                    conexao.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
        }
    }

    /**
     * Método `chamados`. Este método realiza a exibição das Ordens de Serviço
     * (OS) que estão com status "Aberta" e associadas ao ID do usuário
     * especificado. Os resultados são exibidos na tabela `tblChamados` e a
     * aparência da interface é atualizada para refletir o estado atual. Ele
     * também desabilita a interação com alguns elementos da interface e ajusta
     * o alinhamento e estilização das tabelas.
     *
     * Funcionalidades: - **Definição do Status**: - Filtra apenas OS que
     * possuem o status "Aberta".
     *
     * - **Montagem e Execução da Query SQL**: - Recupera informações detalhadas
     * das OS relacionadas ao usuário, incluindo: - Número da OS (`id_os`). -
     * Registro de Empregado (`iduser`). - Nome do funcionário associado. -
     * Setor e prioridade da OS. - Horário inicial da OS.
     *
     * - **Exibição na Tabela**: - Utiliza `DbUtils.resultSetToTableModel(rs)`
     * para exibir os resultados obtidos na tabela `tblChamados`.
     *
     * - **Ajustes da Interface Gráfica**: - Chama `alinhamentoTabela()` para
     * ajustar largura de colunas, alinhamento e altura das linhas. - Atualiza
     * as cores de diversos elementos visuais para destacar o estado atual. -
     * Desabilita o componente `cboAreas` para restringir interações
     * desnecessárias.
     *
     * - **Gerenciamento de Recursos**: - Fecha a conexão com o banco de dados
     * após a execução da consulta, garantindo boa prática de gerenciamento.
     *
     * - **Tratamento de Exceções**: - Captura erros relacionados ao banco de
     * dados (`SQLException`) e exibe mensagens informativas ao usuário.
     *
     * Fluxo do Método: 1. Define o status "Aberta" para filtrar os chamados. 2.
     * Prepara e executa a query SQL com base no ID do usuário e status da OS.
     * 3. Exibe os resultados na tabela e ajusta sua aparência. 4. Atualiza a
     * interface visual para refletir o estado atual. 5. Fecha a conexão com o
     * banco de dados. 6. Captura possíveis erros e exibe mensagens amigáveis ao
     * usuário.
     */
    public void chamados() {

        String status = "Aberta";

        String sql = "select Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where iduser = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
            cboAreas.setEnabled(false);
            alinhamentoTabela();
            rbtSetGeral.setForeground(new Color(0, 0, 51));
            rbtChamados.setForeground(new Color(39, 44, 182));
            rbtChaSolicitados.setForeground(new Color(0, 0, 51));
            lblAtender.setForeground(new Color(39, 44, 182));
            lblChamadosAbertos.setForeground(new Color(0, 0, 51));
            lblGeral.setForeground(new Color(0, 0, 51));
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `chamadosAberto`. Exibe as Ordens de Serviço (OS) com status
     * "Aberta" que foram solicitadas pelo usuário identificado pelo `id`. Os
     * resultados são exibidos na tabela `tblChamados` e ajustes são feitos na
     * interface gráfica para refletir o estado atual.
     *
     * Funcionalidades: - **Filtragem por Status e Solicitante**: - Recupera as
     * OS abertas associadas ao solicitante com `iduser1 = id`. - Filtra com
     * base no status "Aberta".
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para exibir os resultados na tabela `tblChamados`. - Ajusta a aparência
     * da tabela com o método `alinhamentoTabela`.
     *
     * - **Configuração da Interface Gráfica**: - Desativa o componente
     * `cboAreas` para evitar interações desnecessárias. - Atualiza as cores de
     * botões e rótulos para refletir o estado de exibição atual. - Destaca o
     * botão `rbtChaSolicitados` e o rótulo `lblChamadosAbertos`, indicando que
     * eles estão ativos.
     *
     * - **Gerenciamento de Recursos**: - Fecha a conexão com o banco de dados
     * ao final da execução, garantindo boas práticas de uso de recursos.
     *
     * - **Tratamento de Erros**: - Captura erros de `SQLException` e exibe
     * mensagens informativas ao usuário em caso de falha.
     *
     * Fluxo do Método: 1. Define o status "Aberta" para filtrar as OS. 2.
     * Prepara e executa a consulta SQL com base no solicitante (`iduser1 =
     * id`). 3. Exibe os resultados na tabela e ajusta sua aparência. 4.
     * Atualiza os elementos visuais da interface para refletir o estado atual.
     * 5. Fecha a conexão com o banco de dados. 6. Captura possíveis erros e
     * exibe mensagens ao usuário.
     */
    public void chamadosAberto() {

        String status = "Aberta";

        String sql = "select Os.id_os as 'N° OS', Os.iduser as 'R.E', (select nome_usuario from usuarios where iduser = Os.iduser) as 'Funcionário', Os.area_usuario as 'Setor', Os.prioridade_os as 'Prioridade', Os.hora_inicial as 'Início' from os Os where iduser1 = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            tblChamados.setModel(DbUtils.resultSetToTableModel(rs));
            alinhamentoTabela();
            cboAreas.setEnabled(false);
            rbtSetGeral.setForeground(new Color(0, 0, 51));
            rbtChamados.setForeground(new Color(0, 0, 51));
            rbtChaSolicitados.setForeground(new Color(39, 44, 182));

            lblAtender.setForeground(new Color(0, 0, 51));
            lblChamadosAbertos.setForeground(new Color(39, 44, 182));
            lblGeral.setForeground(new Color(0, 0, 51));
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `fitroSetor`. Este método é acionado para verificar e filtrar os
     * dados selecionados na tabela `tblChamados`. Caso o setor do chamado
     * selecionado corresponda ao `id` do usuário, ele inicializa e exibe a tela
     * `TelaOs1`. Além disso, ajusta a interface para maximizar a nova janela e
     * libera os recursos da janela atual.
     *
     * Funcionalidades: - **Obtenção de Dados Selecionados**: - Obtém a linha
     * selecionada na tabela com `getSelectedRow()`. - Recupera o valor da
     * célula na coluna específica (`setores`) usando `getModel().getValueAt()`.
     *
     * - **Verificação de Condição**: - Compara o valor obtido (`setores`) com o
     * identificador do usuário (`id`). - Caso sejam iguais, procede com a
     * inicialização e exibição da tela de detalhes `TelaOs1`.
     *
     * - **Inicialização da Nova Tela**: - Cria uma instância de `TelaOs1` e
     * torna-a visível com `setVisible(true)`. - Adiciona a nova janela ao
     * componente pai (`getParent().add(os)`). - Invoca o método
     * `setOsInicialCham()` para configurar a tela inicial de detalhes da OS.
     *
     * - **Ajuste da Interface**: - Define a nova janela como maximizada com
     * `setMaximum(true)`. - Em caso de erro, captura e trata a exceção
     * `PropertyVetoException` exibindo uma mensagem amigável.
     *
     * - **Encerramento da Janela Atual**: - Libera os recursos da janela atual
     * chamando o método `dispose()`.
     *
     * Fluxo do Método: 1. Obtém os dados da linha selecionada na tabela. 2.
     * Verifica se o setor corresponde ao identificador do usuário. 3.
     * Inicializa e exibe a tela de detalhes `TelaOs1` caso a condição seja
     * atendida. 4. Ajusta a nova janela para maximizar sua exibição. 5. Libera
     * os recursos da janela atual. 6. Captura e trata erros, caso ocorram.
     */
    private void fitroSetor() {

        String setores;
        int setar = tblChamados.getSelectedRow();
        setores = (tblChamados.getModel().getValueAt(setar, 1).toString());
        if (id.equals(setores)) {
            TelaOs1 os = new TelaOs1();
            os.setVisible(true);
            getParent().add(os);
            os.setOsInicialCham();
            try {
                os.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            dispose();
        }
    }

    /**
     * Método `quantideChamados`. Este método realiza a contagem de Ordens de
     * Serviço (OS) com status "Aberta" atribuídas ao usuário identificado pelo
     * `id`. Após calcular a quantidade, exibe o resultado no rótulo
     * `lblAtender`, informando ao usuário o número de OS disponíveis para
     * atendimento.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece a
     * conexão com o banco de dados utilizando o método
     * `ModuloConexao.conector()`.
     *
     * - **Montagem e Execução da Query SQL**: - Realiza uma consulta que conta
     * o número de OS (`COUNT(*)`) que atendem aos critérios: - `iduser = id`:
     * Filtra as OS atribuídas ao usuário atual. - `situacao_os = "Aberta"`:
     * Filtra apenas OS que estão abertas.
     *
     * - **Exibição do Resultado**: - Caso a consulta retorne resultados
     * (`rs.next()`), atualiza o texto do rótulo `lblAtender` com o número total
     * de OS abertas. - Concatena strings para construir dinamicamente a
     * mensagem exibida.
     *
     * - **Gerenciamento de Recursos**: - Após a execução da consulta, fecha a
     * conexão com o banco de dados para evitar problemas de desempenho ou uso
     * excessivo de recursos.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros relacionados ao
     * banco de dados (`SQLException`) e exibe uma mensagem amigável ao usuário.
     *
     * Fluxo do Método: 1. Estabelece a conexão com o banco de dados. 2. Define
     * os critérios para filtrar as OS. 3. Prepara e executa a consulta SQL. 4.
     * Atualiza o rótulo `lblAtender` com o número total de OS abertas. 5. Fecha
     * a conexão com o banco de dados. 6. Captura e trata erros, caso ocorram.
     */
    private void quantideChamados() {
        conexao = ModuloConexao.conector();
        String status = "Aberta";

        String sql = "select count(*) from os  where iduser = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                lblAtender.setText("Para Atender".concat(" : ").concat(rs.getString(1)));
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `quantideChamadosAbertos`. Este método realiza a contagem de
     * Ordens de Serviço (OS) abertas que foram solicitadas pelo usuário
     * identificado pelo `id`. Após calcular a quantidade, exibe o resultado no
     * rótulo `lblChamadosAbertos`, informando o número de chamados abertos.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece uma
     * conexão com o banco de dados utilizando o método
     * `ModuloConexao.conector()`.
     *
     * - **Montagem e Execução da Query SQL**: - Realiza uma contagem de OS
     * (`COUNT(*)`) com base nos critérios: - `iduser1 = id`: Filtra OS
     * solicitadas pelo usuário. - `situacao_os = "Aberta"`: Filtra apenas OS
     * abertas.
     *
     * - **Exibição do Resultado**: - Caso a consulta retorne resultados
     * (`rs.next()`), atualiza o texto do rótulo `lblChamadosAbertos` com o
     * número total de OS abertas. - Usa `concat()` para construir dinamicamente
     * a mensagem exibida.
     *
     * - **Gerenciamento de Recursos**: - Fecha a conexão com o banco de dados
     * ao final da execução para evitar problemas relacionados ao uso excessivo
     * de recursos.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros relacionados ao
     * banco de dados (`SQLException`) e exibe uma mensagem amigável ao usuário
     * em caso de falha.
     *
     * Fluxo do Método: 1. Estabelece a conexão com o banco de dados. 2. Define
     * os critérios para filtrar as OS. 3. Prepara e executa a consulta SQL. 4.
     * Atualiza o rótulo `lblChamadosAbertos` com o número total de chamados
     * abertos. 5. Fecha a conexão com o banco de dados. 6. Captura e trata
     * erros, caso ocorram.
     */
    private void quantideChamadosAbertos() {
        conexao = ModuloConexao.conector();
        String status = "Aberta";

        String sql = "select count(*) from os  where iduser1 = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                lblChamadosAbertos.setText("Chamados Abertos".concat(" : ").concat(rs.getString(1)));
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `quantideGeralSetor`. Este método calcula e exibe o número total
     * de Ordens de Serviço (OS) com status "Aberta" para o setor selecionado.
     * Se o setor for "Todos", ele retorna a contagem total de OS abertas em
     * todos os setores. Caso contrário, realiza a contagem de OS abertas para o
     * setor específico selecionado. O resultado é exibido no rótulo `lblGeral`.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece a
     * conexão com o banco de dados utilizando `ModuloConexao.conector()`.
     *
     * - **Lógica Baseada no Setor**: - Caso `setor.equals("Todos")`, a query
     * SQL considera apenas o status "Aberta" e retorna a contagem total de OS.
     * - Caso contrário, a query SQL filtra pelos critérios: - `area_usuario =
     * setor`: Refere-se ao setor selecionado. - `situacao_os = "Aberta"`:
     * Filtra apenas OS com status "Aberta".
     *
     * - **Exibição do Resultado**: - Recupera o número total de OS do resultado
     * da consulta e atualiza o rótulo `lblGeral` com o valor formatado. - Usa
     * `concat()` para construir dinamicamente a mensagem com o nome do setor e
     * a contagem.
     *
     * - **Gerenciamento de Recursos**: - Fecha a conexão com o banco de dados
     * após a execução da consulta para garantir boa prática.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros relacionados ao
     * banco de dados (`SQLException`) e exibe mensagens informativas ao
     * usuário.
     *
     * Fluxo do Método: 1. Estabelece a conexão com o banco de dados. 2.
     * Verifica se o setor selecionado é "Todos" ou um setor específico. 3.
     * Monta e executa a consulta SQL apropriada. 4. Atualiza o rótulo
     * `lblGeral` com o número total de OS abertas. 5. Fecha a conexão com o
     * banco de dados. 6. Captura e trata erros, caso ocorram.
     */
    private void quantideGeralSetor() {
        conexao = ModuloConexao.conector();
        String status = "Aberta";

        if (setor.equals("Todos")) {

            String sql = "select count(*) from os  where situacao_os = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                rs = pst.executeQuery();
                if (rs.next()) {
                    lblGeral.setText(setor.concat(" : ").concat(rs.getString(1)));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } else {

            String sql = "select count(*) from os  where area_usuario = ? and situacao_os = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, setor);
                pst.setString(2, status);
                rs = pst.executeQuery();
                if (rs.next()) {
                    lblGeral.setText(setor.concat(" : ").concat(rs.getString(1)));
                }
                conexao.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método `fechar`. Este método fecha a janela ou o formulário atual ao
     * invocar o método `dispose()`. Ele é útil para liberar os recursos
     * utilizados pela janela e removê-la da memória, garantindo um
     * gerenciamento eficiente.
     *
     * Funcionalidades: - **Encerramento da Janela**: - `dispose()`: Fecha a
     * janela atual, removendo-a da exibição e liberando recursos relacionados.
     *
     * - **Uso Simples**: - O método pode ser chamado diretamente para fechar a
     * interface atual quando não for mais necessária.
     *
     * Fluxo do Método: 1. Invoca o método `dispose()` para fechar a janela
     * atual. 2. Libera recursos associados à janela.
     */
    public void fechar() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        cboAreas = new javax.swing.JComboBox<>();
        rbtSetGeral = new javax.swing.JRadioButton();
        rbtChamados = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChamados = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        rbtChaSolicitados = new javax.swing.JRadioButton();
        btnAtuTabela = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblAtender = new javax.swing.JLabel();
        lblChamadosAbertos = new javax.swing.JLabel();
        lblGeral = new javax.swing.JLabel();
        JLabel = new JlabelGradient();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setResizable(true);
        setTitle("Chamados");
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 42)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chamados");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(380, 40, 230, 50);

        cboAreas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboAreas.setForeground(new java.awt.Color(0, 0, 51));
        cboAreas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Eventos", "Governança", "Manutenção", "Mordomia" }));
        cboAreas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAreasActionPerformed(evt);
            }
        });
        getContentPane().add(cboAreas);
        cboAreas.setBounds(95, 150, 190, 28);

        buttonGroup1.add(rbtSetGeral);
        rbtSetGeral.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtSetGeral.setForeground(new java.awt.Color(0, 0, 51));
        rbtSetGeral.setText("Todos os Setores");
        rbtSetGeral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtSetGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtSetGeralActionPerformed(evt);
            }
        });
        getContentPane().add(rbtSetGeral);
        rbtSetGeral.setBounds(293, 120, 210, 28);

        buttonGroup1.add(rbtChamados);
        rbtChamados.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtChamados.setForeground(new java.awt.Color(0, 0, 51));
        rbtChamados.setText("Para Atender");
        rbtChamados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtChamados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtChamadosActionPerformed(evt);
            }
        });
        getContentPane().add(rbtChamados);
        rbtChamados.setBounds(513, 120, 210, 28);

        jScrollPane1.setAutoscrolls(true);

        tblChamados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblChamados.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblChamados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Título 5", "Título 6"
            }
        ));
        tblChamados.getTableHeader().setResizingAllowed(false);
        tblChamados.getTableHeader().setReorderingAllowed(false);
        tblChamados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChamadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblChamados);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(55, 200, 890, 400);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Áreas");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(90, 120, 190, 28);

        buttonGroup1.add(rbtChaSolicitados);
        rbtChaSolicitados.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtChaSolicitados.setForeground(new java.awt.Color(0, 0, 51));
        rbtChaSolicitados.setText("Chamado Aberto");
        rbtChaSolicitados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtChaSolicitados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtChaSolicitadosActionPerformed(evt);
            }
        });
        getContentPane().add(rbtChaSolicitados);
        rbtChaSolicitados.setBounds(733, 120, 210, 28);

        btnAtuTabela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/atualizar24px.png"))); // NOI18N
        btnAtuTabela.setToolTipText("Atualizar ");
        btnAtuTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtuTabelaActionPerformed(evt);
            }
        });
        getContentPane().add(btnAtuTabela);
        btnAtuTabela.setBounds(55, 150, 28, 28);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48760_file_file.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(616, 33, 64, 64);

        lblAtender.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAtender.setForeground(new java.awt.Color(39, 44, 182));
        lblAtender.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAtender.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblAtender.setOpaque(true);
        getContentPane().add(lblAtender);
        lblAtender.setBounds(513, 150, 210, 30);

        lblChamadosAbertos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChamadosAbertos.setForeground(new java.awt.Color(39, 44, 182));
        lblChamadosAbertos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChamadosAbertos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblChamadosAbertos.setOpaque(true);
        getContentPane().add(lblChamadosAbertos);
        lblChamadosAbertos.setBounds(733, 150, 210, 30);

        lblGeral.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGeral.setForeground(new java.awt.Color(39, 44, 182));
        lblGeral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGeral.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblGeral.setOpaque(true);
        getContentPane().add(lblGeral);
        lblGeral.setBounds(293, 150, 210, 30);
        getContentPane().add(JLabel);
        JLabel.setBounds(-2, -2, 1000, 650);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `formInternalFrameOpened`. Este método é executado automaticamente
     * quando a janela interna (`JInternalFrame`) é aberta. Ele realiza diversas
     * configurações e atualizações iniciais da interface gráfica para garantir
     * que os dados exibidos estejam corretamente organizados e alinhados.
     * Também define o comportamento da tabela de chamados e inicializa
     * diferentes contadores de dados relacionados às Ordens de Serviço (OS).
     *
     * Funcionalidades: - **Configuração de Seleção da Tabela**: -
     * `tblChamados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)`:
     * Permite a seleção de apenas uma linha por vez na tabela de chamados.
     *
     * - **Ajuste Visual da Tabela**: - `alinhamentoTabela()`: Configura as
     * colunas da tabela `tblChamados` com larguras apropriadas, alinha os dados
     * e define a altura das linhas para facilitar a leitura.
     *
     * - **Filtragem e Exibição de OS**: - `osSetores()`: Filtra e exibe as OS
     * relacionadas a diferentes setores com base no estado atual da interface.
     *
     * - **Atualização de Contadores**: - `quantideChamados()`: Conta e exibe a
     * quantidade de OS abertas atribuídas ao usuário responsável. -
     * `quantideChamadosAbertos()`: Conta e exibe a quantidade de chamados
     * abertos solicitados pelo usuário. - `quantideGeralSetor()`: Atualiza
     * contadores relacionados ao setor geral, fornecendo uma visão consolidada
     * das OS.
     *
     * - **TODO Placeholder**: - Linhas comentadas sugerem funcionalidades
     * adicionais para configuração inicial que podem ser implementadas no
     * futuro.
     *
     * Fluxo do Método: 1. Define o modo de seleção da tabela. 2. Alinha
     * visualmente as colunas e linhas da tabela. 3. Carrega as OS relacionadas
     * a setores. 4. Atualiza os contadores de chamados com base nos dados
     * atuais.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        tblChamados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alinhamentoTabela();
        osSetores();
        quantideChamados();
        quantideChamadosAbertos();
        quantideGeralSetor();

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método `rbtChamadosActionPerformed`. Este método é acionado quando o
     * botão de rádio `rbtChamados` é selecionado na interface gráfica. Ele
     * ajusta as configurações visuais dos botões e rótulos para refletir o
     * estado ativo do botão, estabelece uma conexão com o banco de dados e
     * invoca o método `chamados` para exibir as Ordens de Serviço (OS) abertas
     * atribuídas ao usuário.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece conexão
     * utilizando o método `ModuloConexao.conector()`, garantindo acesso aos
     * dados das OS.
     *
     * - **Ajuste de Elementos Visuais**: - Atualiza as cores dos botões de
     * rádio para indicar o botão selecionado: - Destaca `rbtChamados` com cor
     * azul escuro. - Retorna `rbtSetGeral` e `rbtChaSolicitados` à cor padrão
     * preta. - Atualiza as cores dos rótulos para refletir o estado ativo: -
     * Destaca `lblAtender` com cor azul escuro. - Define `lblChamadosAbertos` e
     * `lblGeral` com cor preta.
     *
     * - **Atualização da Tabela**: - Invoca o método `chamados` para carregar
     * na tabela as OS abertas atribuídas ao usuário atual.
     *
     * Fluxo do Método: 1. Estabelece conexão com o banco de dados. 2. Atualiza
     * as cores dos botões e rótulos da interface. 3. Carrega os chamados
     * abertos atribuídos ao usuário atual na tabela.
     */
    private void rbtChamadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtChamadosActionPerformed

        conexao = ModuloConexao.conector();
        rbtSetGeral.setForeground(new Color(0, 0, 51));
        rbtChamados.setForeground(new Color(39, 44, 182));
        rbtChaSolicitados.setForeground(new Color(0, 0, 51));

        lblAtender.setForeground(new Color(39, 44, 182));
        lblChamadosAbertos.setForeground(new Color(0, 0, 51));
        lblGeral.setForeground(new Color(0, 0, 51));

        chamados();
    }//GEN-LAST:event_rbtChamadosActionPerformed

    /**
     * Método `rbtSetGeralActionPerformed`. Este método é acionado quando o
     * botão de rádio `rbtSetGeral` é selecionado na interface gráfica. Ele
     * realiza configurações visuais na interface, habilita a interação com a
     * combobox de áreas (`cboAreas`) e invoca o método `osSetores` para
     * atualizar os dados exibidos na tabela de chamados com base no setor.
     *
     * Funcionalidades: - **Configuração da Interface Gráfica**: - Habilita a
     * combobox `cboAreas` para permitir a interação do usuário. - Atualiza as
     * cores dos rótulos e botões da interface para refletir a seleção atual: -
     * `rbtSetGeral` é destacado com cor azul escuro. - `rbtChamados` e
     * `rbtChaSolicitados` retornam ao estilo padrão com cor preta. - Outros
     * rótulos (`lblAtender`, `lblChamadosAbertos`, `lblGeral`) recebem cores
     * diferentes para indicar o estado ativo.
     *
     * - **Atualização de Dados**: - Chama o método `osSetores` para atualizar
     * os chamados exibidos na tabela com base no setor selecionado.
     *
     * - **Conexão com o Banco de Dados**: - Estabelece a conexão com o banco de
     * dados através de `ModuloConexao.conector()` para garantir que os dados
     * exibidos sejam atualizados corretamente.
     *
     * Fluxo do Método: 1. Habilita a combobox de áreas. 2. Atualiza as cores
     * dos botões e rótulos da interface. 3. Invoca o método `osSetores` para
     * carregar os chamados correspondentes.
     */
    private void rbtSetGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtSetGeralActionPerformed

        conexao = ModuloConexao.conector();
        cboAreas.setEnabled(true);
        rbtSetGeral.setForeground(new Color(39, 44, 182));
        rbtChamados.setForeground(new Color(0, 0, 51));
        rbtChaSolicitados.setForeground(new Color(0, 0, 51));

        lblAtender.setForeground(new Color(0, 0, 51));
        lblChamadosAbertos.setForeground(new Color(0, 0, 51));
        lblGeral.setForeground(new Color(39, 44, 182));

        osSetores();
    }//GEN-LAST:event_rbtSetGeralActionPerformed

    /**
     * Método `rbtChaSolicitadosActionPerformed`. Este método é acionado quando
     * o botão de rádio `rbtChaSolicitados` é selecionado na interface gráfica.
     * Ele atualiza a interface visual, desativa a combobox de áreas
     * (`cboAreas`) e chama o método `chamadosAberto` para exibir os chamados
     * abertos solicitados pelo usuário.
     *
     * Funcionalidades: - **Configuração da Interface Gráfica**: - Desabilita a
     * combobox `cboAreas`, restringindo a interação para evitar alterações
     * desnecessárias. - Atualiza as cores dos botões de rádio e rótulos para
     * refletir o estado atual: - Destaca o botão `rbtChaSolicitados` com cor
     * azul escuro. - Os outros botões (`rbtSetGeral` e `rbtChamados`) retornam
     * à cor preta. - Atualiza os rótulos, destacando `lblChamadosAbertos` para
     * indicar que está ativo.
     *
     * - **Atualização de Dados**: - Chama o método `chamadosAberto` para
     * carregar e exibir na tabela os chamados abertos solicitados pelo usuário.
     *
     * - **Conexão com o Banco de Dados**: - Estabelece conexão com o banco de
     * dados através de `ModuloConexao.conector()` para garantir que os dados
     * exibidos estejam atualizados.
     *
     * Fluxo do Método: 1. Conecta ao banco de dados. 2. Desabilita a combobox
     * de áreas. 3. Atualiza as cores dos botões e rótulos para refletir a
     * seleção atual. 4. Invoca o método `chamadosAberto` para exibir os
     * chamados solicitados.
     */
    private void rbtChaSolicitadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtChaSolicitadosActionPerformed

        conexao = ModuloConexao.conector();
        cboAreas.setEnabled(false);
        rbtSetGeral.setForeground(new Color(0, 0, 51));
        rbtChamados.setForeground(new Color(0, 0, 51));
        rbtChaSolicitados.setForeground(new Color(39, 44, 182));

        lblAtender.setForeground(new Color(0, 0, 51));
        lblChamadosAbertos.setForeground(new Color(39, 44, 182));
        lblGeral.setForeground(new Color(0, 0, 51));

        chamadosAberto();
    }//GEN-LAST:event_rbtChaSolicitadosActionPerformed

    /**
     * Método `btnAtuTabelaActionPerformed`. Este método é acionado quando o
     * botão de atualização da tabela (`btnAtuTabela`) é pressionado. Ele
     * atualiza os dados exibidos na tabela de chamados com base no estado atual
     * dos setores e configurações da interface gráfica. Também atualiza os
     * contadores relacionados aos chamados e ajusta os estilos visuais dos
     * elementos.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece conexão
     * com o banco de dados utilizando o método `ModuloConexao.conector()`,
     * garantindo acesso confiável aos dados.
     *
     * - **Habilitação da Interação**: - Habilita o componente `cboAreas` para
     * permitir ao usuário ajustar áreas na interface gráfica.
     *
     * - **Atualização Visual**: - Define o botão de rádio `rbtSetGeral` como
     * selecionado (`setSelected(true)`). - Atualiza as cores dos botões de
     * rádio (`rbtSetGeral`, `rbtChamados`, `rbtChaSolicitados`) e rótulos
     * (`lblAtender`, `lblChamadosAbertos`, `lblGeral`) para refletir o estado
     * atual.
     *
     * - **Atualização dos Dados**: - Invoca o método `osSetores()` para
     * carregar os chamados associados ao setor atual. - Atualiza os contadores
     * de chamados com: - `quantideChamados()`: Contagem de OS abertas
     * atribuídas ao usuário. - `quantideChamadosAbertos()`: Contagem de
     * chamados abertos solicitados pelo usuário. - `quantideGeralSetor()`:
     * Atualização dos contadores gerais relacionados ao setor.
     *
     * Fluxo do Método: 1. Estabelece conexão com o banco de dados. 2. Habilita
     * interação com `cboAreas`. 3. Atualiza as cores dos botões e rótulos da
     * interface. 4. Carrega os dados dos setores e atualiza os contadores de
     * chamados. 5. Finaliza o processo garantindo que a interface reflita as
     * atualizações.
     */
    private void btnAtuTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtuTabelaActionPerformed

        conexao = ModuloConexao.conector();
        cboAreas.setEnabled(true);
        rbtSetGeral.setForeground(new Color(39, 44, 182));
        rbtSetGeral.setSelected(true);
        rbtChamados.setForeground(new Color(0, 0, 51));
        rbtChaSolicitados.setForeground(new Color(0, 0, 51));

        lblAtender.setForeground(new Color(0, 0, 51));
        lblChamadosAbertos.setForeground(new Color(0, 0, 51));
        lblGeral.setForeground(new Color(39, 44, 182));

        osSetores();
        quantideChamados();
        quantideChamadosAbertos();
        quantideGeralSetor();
    }//GEN-LAST:event_btnAtuTabelaActionPerformed

    /**
     * Método `cboAreasActionPerformed`. Este método é acionado automaticamente
     * quando o usuário realiza uma seleção na combobox `cboAreas`. Ele atualiza
     * o setor selecionado, conecta ao banco de dados, e invoca métodos para
     * carregar os chamados e atualizar contadores relacionados ao setor
     * selecionado.
     *
     * Funcionalidades: - **Conexão com o Banco de Dados**: - Estabelece conexão
     * utilizando o método `ModuloConexao.conector()` para garantir acesso
     * confiável aos dados.
     *
     * - **Atualização de Setor**: - Recupera o setor selecionado pelo usuário
     * na combobox `cboAreas` com `getSelectedItem().toString()`. - Atribui o
     * valor à variável `setor` para filtrar dados conforme o setor escolhido.
     *
     * - **Atualização de Dados e Contadores**: - Invoca o método `osSetores()`
     * para carregar os chamados relacionados ao setor selecionado e exibir na
     * tabela. - Chama o método `quantideGeralSetor()` para atualizar os
     * contadores gerais relacionados ao setor.
     *
     * Fluxo do Método: 1. Estabelece a conexão com o banco de dados. 2.
     * Atualiza a variável `setor` com o valor selecionado na combobox. 3.
     * Carrega os chamados do setor com o método `osSetores()`. 4. Atualiza os
     * contadores gerais do setor com o método `quantideGeralSetor()`.
     */
    private void cboAreasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAreasActionPerformed

        conexao = ModuloConexao.conector();
        setor = cboAreas.getSelectedItem().toString();
        osSetores();
        quantideGeralSetor();
    }//GEN-LAST:event_cboAreasActionPerformed

    /**
     * Método `tblChamadosMouseClicked`. Este método é acionado automaticamente
     * quando o usuário clica em uma linha da tabela `tblChamados`. Ele realiza
     * uma ação específica ao chamar o método `fitroSetor()`, permitindo filtrar
     * os dados exibidos com base no setor selecionado ou na linha clicada.
     *
     * Funcionalidades: - **Detecção de Clique do Usuário**: - O evento
     * `MouseClicked` é disparado quando o usuário clica em qualquer linha da
     * tabela `tblChamados`.
     *
     * - **Filtragem por Setor**: - Chama o método `fitroSetor()` para realizar
     * uma ação personalizada com base no setor associado ao item selecionado na
     * tabela. - Esse método possibilita atualizar os dados ou realizar outras
     * ações com base no setor ou na linha clicada.
     *
     * - **Extensibilidade**: - O comentário `TODO` indica que o desenvolvedor
     * pode adicionar mais lógica ou ajustes específicos caso necessário.
     *
     * Fluxo do Método: 1. Detecta o clique do usuário em uma linha da tabela
     * `tblChamados`. 2. Chama o método `fitroSetor()` para processar os dados
     * com base no setor ou na linha clicada.
     */
    private void tblChamadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChamadosMouseClicked

        fitroSetor();
    }//GEN-LAST:event_tblChamadosMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel;
    private javax.swing.JButton btnAtuTabela;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboAreas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAtender;
    private javax.swing.JLabel lblChamadosAbertos;
    private javax.swing.JLabel lblGeral;
    public static javax.swing.JRadioButton rbtChaSolicitados;
    public static javax.swing.JRadioButton rbtChamados;
    private javax.swing.JRadioButton rbtSetGeral;
    public static javax.swing.JTable tblChamados;
    // End of variables declaration//GEN-END:variables
}
