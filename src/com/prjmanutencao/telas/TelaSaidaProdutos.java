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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultFormatter;
import net.proteanit.sql.DbUtils;

/**
 * Classe TelaSaidaProdutos.
 *
 * Representa uma interface gráfica interna (`JInternalFrame`) responsável pela
 * saída de produtos em um sistema de controle de estoque ou gestão de
 * mercadorias. Esta classe gerencia operações relacionadas à retirada de
 * produtos, como cálculos, validações e atualizações de banco de dados.
 *
 * Atributos principais: - `Connection conexao`: Conexão com o banco de dados. -
 * `PreparedStatement pst`: Comando SQL preparado para execução. - `ResultSet
 * rs`: Conjunto de resultados de consultas SQL. - `StringBuffer buffer, nf,
 * cod`: Buffers utilizados para manipulação de dados textuais, como nota fiscal
 * e código. - `int retirada, calculo, total, limite`: Variáveis de controle
 * para operações de saída de produtos.
 *
 * Funcionalidades esperadas: - Realizar cálculos de retirada de produtos. -
 * Validar limites e quantidades disponíveis. - Atualizar registros no banco de
 * dados. - Interagir com componentes visuais para exibir e modificar dados.
 *
 * Observações: - A classe estende `javax.swing.JInternalFrame`, sendo ideal
 * para uso em aplicações com `JDesktopPane`. - Recomendado encapsular os
 * atributos com modificadores de acesso apropriados e utilizar métodos
 * getter/setter conforme necessário.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 *
 */
public class TelaSaidaProdutos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    StringBuffer buffer;
    StringBuffer nf;
    StringBuffer cod;
    int retirada, calculo;
    int total;
    int limite;

    /**
     * Construtor da classe TelaSaidaProdutos.
     *
     * Inicializa os componentes gráficos da interface e estabelece a conexão
     * com o banco de dados.
     *
     * Ações realizadas: - Chama o método `initComponents()` para configurar os
     * elementos visuais da interface. - Inicializa o atributo `conexao`
     * utilizando o método `ModuloConexao.conector()`, que retorna uma instância
     * de `Connection` conectada ao banco de dados.
     *
     * Requisitos: - A classe `ModuloConexao` deve fornecer corretamente o
     * método `conector()` para garantir a conexão com o banco.
     *
     * Possíveis melhorias: - Verificar se a conexão foi estabelecida com
     * sucesso e tratar falhas de conexão. - Adicionar mensagens de log ou
     * feedback visual ao usuário em caso de erro.
     */
    public TelaSaidaProdutos() {
        initComponents();
        conexao = ModuloConexao.conector();

    }

    /**
     * Classe JlabelGradient.
     *
     * Esta classe estende JLabel para criar um rótulo com fundo em gradiente. O
     * gradiente é desenhado verticalmente, indo de uma cor azul translúcida
     * para branco, proporcionando um efeito visual suave.
     *
     * A principal personalização é feita sobrescrevendo o método
     * paintComponent, onde o gradiente é aplicado antes de qualquer outro
     * conteúdo do componente.
     *
     * Utiliza: - Graphics2D para renderização avançada. - GradientPaint para
     * criar o efeito de gradiente.
     *
     * Observação: Esta classe pode ser usada para melhorar a estética de
     * interfaces gráficas em aplicações Swing.
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
     * Ajusta a aparência da tabela de produtos (`tblRetProd`) na interface
     * gráfica.
     *
     * Este método configura o cabeçalho da tabela, define larguras específicas
     * para cada coluna, centraliza o texto do cabeçalho e ajusta a altura das
     * linhas. Também invoca o método auxiliar `ajustarTabelaNf()` para aplicar
     * ajustes adicionais relacionados à nota fiscal.
     *
     * Ações realizadas: - Define a altura do cabeçalho da tabela como 30
     * pixels. - Aplica fonte Arial em negrito, tamanho 12, com cor azul escura
     * ao cabeçalho. - Centraliza o texto do cabeçalho. - Define larguras
     * específicas para as colunas: - Coluna 0: 90 pixels - Coluna 1: 240 pixels
     * - Coluna 2: 90 pixels - Coluna 3: 70 pixels - Define a altura das linhas
     * da tabela como 22 pixels. - Chama `ajustarTabelaNf()` para ajustes
     * adicionais.
     *
     * Tratamento de erros: - Em caso de exceção, exibe uma mensagem de erro via
     * `JOptionPane`.
     *
     * Observações: - Certifique-se de que `tblRetProd` esteja corretamente
     * inicializada antes da chamada. - O método `ajustarTabelaNf()` deve estar
     * implementado na mesma classe.
     */
    private void ajustarTabela() {

        int width = 90, width1 = 240, width2 = 90, width3 = 70;
        JTableHeader header;

        try {
            header = tblRetProd.getTableHeader();
            header.setPreferredSize(new Dimension(0, 30));
            header.setFont(new Font("Arial", Font.BOLD, 12));
            header.setForeground(new Color(0, 0, 51));
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblRetProd.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblRetProd.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblRetProd.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblRetProd.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblRetProd.setRowHeight(22);
            ajustarTabelaNf();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar a Tabela produtos" + "\n" + e);
        }
    }

    /**
     * Ajusta a aparência da tabela de notas fiscais (`tblRetProdNf`) na
     * interface gráfica.
     *
     * Este método configura o cabeçalho da tabela, define larguras específicas
     * para as colunas, centraliza o texto do cabeçalho e aplica fonte padrão às
     * células da tabela.
     *
     * Ações realizadas: - Define a altura do cabeçalho da tabela como 30
     * pixels. - Aplica fonte Arial em negrito, tamanho 12, com cor azul escura
     * ao cabeçalho. - Centraliza o texto do cabeçalho. - Define larguras
     * específicas para as colunas: - Coluna 0: 350 pixels - Coluna 1: 80 pixels
     * - Define a fonte das células da tabela como Arial, tamanho 11, estilo
     * normal.
     *
     * Tratamento de erros: - Em caso de exceção, exibe uma mensagem de erro via
     * `JOptionPane`.
     *
     * Observações: - Certifique-se de que `tblRetProdNf` esteja corretamente
     * inicializada antes da chamada. - Este método é chamado por
     * `ajustarTabela()` para garantir consistência visual entre as tabelas.
     */
    private void ajustarTabelaNf() {

        int width = 350, width1 = 80;
        JTableHeader header;

        try {
            header = tblRetProdNf.getTableHeader();
            header.setPreferredSize(new Dimension(0, 30));
            header.setFont(new Font("Arial", Font.BOLD, 12));
            header.setForeground(new Color(0, 0, 51));
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblRetProdNf.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblRetProdNf.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblRetProdNf.setFont(new Font("Arial", Font.PLAIN, 11));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar a Tabela Nota Fiscal" + "\n" + e);
        }
    }

    /**
     * Classe alinhar.
     *
     * Renderizador personalizado para células de uma tabela (`JTable`),
     * utilizado para centralizar o conteúdo das células e aplicar estilo visual
     * consistente.
     *
     * Esta classe estende `DefaultTableCellRenderer` e sobrescreve o método
     * `getTableCellRendererComponent` para modificar a aparência das células da
     * tabela.
     *
     * Funcionalidades: - Centraliza horizontalmente o conteúdo das células. -
     * Define a altura das linhas da tabela como 48 pixels. - Aplica fonte
     * Arial, tamanho 12, estilo normal às células.
     *
     * Observações importantes: - A chamada `tblRetProd.setRowHeight(48)` dentro
     * do renderizador pode causar efeitos colaterais, pois altera a altura da
     * linha da tabela toda vez que uma célula é renderizada. É mais eficiente
     * definir a altura da linha fora do renderizador, em um método de
     * configuração da tabela. - O uso de `setHorizontalAlignment(CENTER)` afeta
     * o próprio renderizador, mas o retorno é o `label`. Para consistência,
     * recomenda-se aplicar as configurações diretamente ao `label`.
     *
     * Exemplo de uso: ```java
     * tblRetProd.getColumnModel().getColumn(0).setCellRenderer(new alinhar());
     * ```
     */
    public class alinhar extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            tblRetProd.setRowHeight(48);
            setFont(new Font("Arial", Font.PLAIN, 12));
            return label;

        }
    }

    /**
     * Classe alinharTabNf.
     *
     * Renderizador personalizado para células da tabela de notas fiscais
     * (`tblRetProdNf`), responsável por centralizar o conteúdo e aplicar estilo
     * visual padronizado.
     *
     * Esta classe estende `DefaultTableCellRenderer` e sobrescreve o método
     * `getTableCellRendererComponent` para modificar a aparência das células.
     *
     * Funcionalidades: - Centraliza horizontalmente o conteúdo das células. -
     * Aplica fonte Arial, tamanho 11, estilo normal.
     *
     * Parâmetros do método sobrescrito: - `table`: A tabela que está sendo
     * renderizada. - `value`: O valor da célula a ser exibido. - `isSelected`:
     * Indica se a célula está selecionada. - `hasFocus`: Indica se a célula tem
     * foco. - `row`: Índice da linha. - `column`: Índice da coluna.
     *
     * Observações: - O método retorna um `JLabel` com as configurações visuais
     * aplicadas. - Ideal para uso em colunas da tabela onde se deseja
     * alinhamento central e fonte discreta.
     *
     * Exemplo de uso: ```java
     * tblRetProdNf.getColumnModel().getColumn(0).setCellRenderer(new
     * alinharTabNf()); ```
     */
    public class alinharTabNf extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Arial", Font.PLAIN, 11));
            return label;

        }
    }

    /**
     * Método setorAcesso.
     *
     * Recupera a área de acesso do usuário e inicializa a tabela de produtos.
     *
     * Este método realiza uma consulta ao banco de dados para obter o setor
     * (área) associado ao usuário identificado por `id`. O resultado é
     * armazenado no `StringBuffer buffer`, e caso haja retorno, o método
     * `tabelaProdInicio()` é chamado para carregar os dados iniciais da tabela.
     *
     * Ações realizadas: - Estabelece conexão com o banco de dados via
     * `ModuloConexao.conector()`. - Executa a query: `SELECT area_usuario FROM
     * usuarios WHERE iduser = ?`. - Insere o resultado da consulta no `buffer`.
     * - Chama `tabelaProdInicio()` se houver resultado.
     *
     * Tratamento de erros: - Em caso de falha na execução da consulta, exibe
     * uma mensagem de erro via `JOptionPane`.
     *
     * Requisitos: - A variável `id` deve estar corretamente inicializada antes
     * da execução. - O método `ModuloConexao.conector()` deve retornar uma
     * conexão válida. - O método `tabelaProdInicio()` deve estar implementado e
     * acessível.
     *
     * Observações: - O uso de `StringBuffer` é seguro para concorrência, mas se
     * não houver necessidade de sincronização, considere usar `StringBuilder`
     * para melhor desempenho. - A conexão é reaberta no início do método, o que
     * pode ser redundante se já estiver ativa.
     */
    private void setorAcesso() {

        conexao = ModuloConexao.conector();
        buffer = new StringBuffer();
        String sql = "select area_usuario from usuarios where iduser = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                buffer.append(rs.getString(1));
                tabelaProdInicio();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método tabelaProdInicioPesq.
     *
     * Realiza uma pesquisa de produtos com base no setor e no texto digitado
     * pelo usuário.
     *
     * Este método consulta o banco de dados para buscar produtos cujo nome ou
     * código comecem com o texto informado em `txtRetProdPesqui`, filtrando
     * também pelo setor armazenado em `buffer`.
     *
     * Ações realizadas: - Estabelece conexão com o banco de dados. - Executa a
     * query com filtros por setor e nome/código. - Cria um `DefaultTableModel`
     * com colunas: Código, Produto, Quantidade, Imagem. - Define o modelo da
     * tabela `tblRetProd`. - Aplica o renderizador personalizado
     * `ImageRenderer1` para exibir imagens. - Adiciona os dados da consulta ao
     * modelo, convertendo imagens em `ImageIcon`. - Chama `ajustarTabela()`
     * para aplicar formatação visual. - Fecha a conexão após o uso.
     *
     * Observações: - A cláusula `OR` na SQL pode causar resultados inesperados
     * se não agrupada corretamente. Recomenda-se usar parênteses para garantir
     * a lógica: ```sql WHERE (setorProduto = ? AND nomeProduto LIKE ?) OR
     * (setorProduto = ? AND cod LIKE ?) ``` - O método assume que
     * `txtRetProdPesqui` e `buffer` estão corretamente inicializados.
     */
    private void tabelaProdInicioPesq() {

        conexao = ModuloConexao.conector();

        String sql = "select cod, nomeProduto, quantidadeProduto, imagemProduto from cadastro_prod where setorProduto = ? and nomeProduto like ? or setorProduto = ? and cod like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, buffer.toString());
            pst.setString(2, txtRetProdPesqui.getText() + "%");
            pst.setString(3, buffer.toString());
            pst.setString(4, txtRetProdPesqui.getText() + "%");
            rs = pst.executeQuery();
            // Cria um DefaultTableModel e adiciona uma coluna para as imagens
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Código");
            model.addColumn("Produto");
            model.addColumn("Quantidade");
            model.addColumn("Imagem");

            // Cria um JTable e define o seu modelo
            tblRetProd.setModel(model);

            // Define um renderizador personalizado para a coluna da imagem
            tblRetProd.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer1());

            // Recupera e adiciona as imagens ao modelo
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(4);
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    new ImageIcon(imgBytes)
                });
                ajustarTabela();
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método tabelaProdInicio.
     *
     * Carrega os produtos iniciais do setor atual na tabela `tblRetProd`.
     *
     * Este método consulta o banco de dados para obter todos os produtos
     * associados ao setor armazenado em `buffer`, e exibe os resultados na
     * tabela com imagens.
     *
     * Ações realizadas: - Executa a query: `SELECT cod, nomeProduto,
     * quantidadeProduto, imagemProduto FROM cadastro_prod WHERE setorProduto =
     * ?`. - Cria um `DefaultTableModel` com colunas: Código, Produto,
     * Quantidade, Imagem. - Define o modelo da tabela `tblRetProd`. - Aplica o
     * renderizador `ImageRenderer1` para a coluna de imagem. - Adiciona os
     * dados ao modelo, convertendo imagens em `ImageIcon`. - Chama
     * `ajustarTabela()` para aplicar formatação visual. - Fecha a conexão após
     * o uso.
     *
     * Observações: - Ideal para exibir todos os produtos do setor sem filtro de
     * pesquisa. - O método depende de `buffer` estar corretamente preenchido.
     */
    private void tabelaProdInicio() {

        String sql = "select cod, nomeProduto, quantidadeProduto, imagemProduto from cadastro_prod where setorProduto = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, buffer.toString());
            rs = pst.executeQuery();
            // Cria um DefaultTableModel e adiciona uma coluna para as imagens
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Código");
            model.addColumn("Produto");
            model.addColumn("Quantidade");
            model.addColumn("Imagem");

            // Cria um JTable e define o seu modelo
            tblRetProd.setModel(model);

            // Define um renderizador personalizado para a coluna da imagem
            tblRetProd.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer1());

            // Recupera e adiciona as imagens ao modelo
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(4);
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    new ImageIcon(imgBytes)
                });
                ajustarTabela();
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método setProd.
     *
     * Define os dados do produto selecionado na tabela `tblRetProd` nos campos
     * de texto da interface.
     *
     * Este método captura a linha atualmente selecionada na tabela de produtos
     * e extrai os valores das colunas de código e quantidade total, preenchendo
     * os campos `txtRetProdCod` e `txtRetProdQuntTotal`. Após isso, chama o
     * método `tabelaProdutosNf()` para atualizar a tabela de notas fiscais
     * relacionada.
     *
     * Ações realizadas: - Obtém o índice da linha selecionada na tabela
     * `tblRetProd`. - Extrai o valor da coluna 0 (código do produto) e define
     * em `txtRetProdCod`. - Extrai o valor da coluna 2 (quantidade do produto)
     * e define em `txtRetProdQuntTotal`. - Chama `tabelaProdutosNf()` para
     * atualizar os dados vinculados à nota fiscal.
     *
     * Requisitos: - A tabela `tblRetProd` deve conter dados e ter uma linha
     * selecionada. - Os campos `txtRetProdCod` e `txtRetProdQuntTotal` devem
     * estar inicializados. - O método `tabelaProdutosNf()` deve estar
     * implementado e acessível.
     *
     * Observações: - Não há verificação se `setar` é válido (por exemplo, se
     * nenhuma linha estiver selecionada). Recomenda-se adicionar uma
     * verificação: ```java if (setar >= 0) { ... } ```
     */
    private void setProd() {
        int setar = tblRetProd.getSelectedRow();
        txtRetProdCod.setText(tblRetProd.getModel().getValueAt(setar, 0).toString());
        txtRetProdQuntTotal.setText(tblRetProd.getModel().getValueAt(setar, 2).toString());
        tabelaProdutosNf();
    }

    /**
     * Método tabelaProdutosNf.
     *
     * Carrega os dados de notas fiscais e validade dos produtos selecionados na
     * tabela `tblRetProd`.
     *
     * Este método realiza uma consulta ao banco de dados para buscar registros
     * da tabela `produ_entrada` que correspondem ao código do produto
     * atualmente selecionado e que possuem quantidade disponível. Os resultados
     * são exibidos na tabela `tblRetProdNf`.
     *
     * Ações realizadas: - Estabelece conexão com o banco de dados via
     * `ModuloConexao.conector()`. - Executa a query: ```sql SELECT nfProduto AS
     * 'Nota Fiscal', dataValid AS 'Validade' FROM produ_entrada WHERE cod = ?
     * AND quantidadeTotal > 0 ``` - Define o modelo da tabela `tblRetProdNf`
     * com os dados retornados usando `DbUtils.resultSetToTableModel(rs)`. -
     * Chama `ajustarTabela()` para aplicar formatação visual à tabela. - Fecha
     * a conexão após o uso.
     *
     * Requisitos: - O campo `txtRetProdCod` deve conter o código do produto
     * selecionado. - A tabela `tblRetProdNf` deve estar inicializada. - O
     * método `ajustarTabela()` deve estar implementado e acessível.
     *
     * Observações: - O uso de `DbUtils.resultSetToTableModel(rs)` facilita a
     * conversão direta dos dados do banco para a tabela. - Recomenda-se usar
     * `try-with-resources` para garantir o fechamento automático da conexão e
     * do `PreparedStatement`. - O método não trata o caso em que nenhum
     * resultado é retornado — pode ser útil limpar ou informar o usuário.
     */
    private void tabelaProdutosNf() {

        conexao = ModuloConexao.conector();

        String sql = "select nfProduto as 'Nota Fiscal', dataValid as 'Validade' from produ_entrada where cod = ? and quantidadeTotal > 0";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtRetProdCod.getText());
            rs = pst.executeQuery();
            tblRetProdNf.setModel(DbUtils.resultSetToTableModel(rs));
            ajustarTabela();
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método setProdNf.
     *
     * Define os dados da nota fiscal selecionada na tabela `tblRetProdNf`.
     *
     * Este método captura a linha atualmente selecionada na tabela de notas
     * fiscais e extrai o valor da coluna correspondente à nota fiscal,
     * armazenando-o no `StringBuffer nf`. Em seguida, chama o método
     * `produto()` para realizar ações relacionadas ao produto vinculado à nota
     * fiscal.
     *
     * Ações realizadas: - Inicializa os buffers `nf` e `cod`. - Obtém o índice
     * da linha selecionada na tabela `tblRetProdNf`. - Extrai o valor da coluna
     * 0 (nota fiscal) e armazena em `nf`. - Chama o método `produto()` para
     * processar os dados do produto.
     *
     * Requisitos: - A tabela `tblRetProdNf` deve conter dados e ter uma linha
     * selecionada. - O método `produto()` deve estar implementado e acessível.
     *
     * Observações: - Não há verificação se `setar` é válido (por exemplo, se
     * nenhuma linha estiver selecionada). Recomenda-se adicionar uma
     * verificação: ```java if (setar >= 0) { ... } ``` - O buffer `cod` é
     * inicializado mas não utilizado neste método — pode ser redundante.
     */
    private void setProdNf() {

        nf = new StringBuffer();
        cod = new StringBuffer();

        int setar = tblRetProdNf.getSelectedRow();
        nf.append(tblRetProdNf.getModel().getValueAt(setar, 0).toString());
        produto();
    }

    /**
     * Método produto.
     *
     * Recupera o código interno do produto com base na nota fiscal selecionada.
     *
     * Este método realiza uma consulta ao banco de dados na tabela
     * `produ_entrada` para obter o valor de `nfCod` correspondente à nota
     * fiscal armazenada em `nf`. O resultado é armazenado no `StringBuffer
     * cod`. Em seguida, chama o método `saidaProdutosNf()` para processar a
     * saída dos produtos vinculados.
     *
     * Ações realizadas: - Estabelece conexão com o banco de dados via
     * `ModuloConexao.conector()`. - Executa a query: ```sql SELECT nfCod FROM
     * produ_entrada WHERE nfProduto = ? ``` - Armazena o resultado em `cod` se
     * houver retorno. - Chama `saidaProdutosNf()` para continuar o fluxo de
     * saída de produtos.
     *
     * Requisitos: - O buffer `nf` deve conter a nota fiscal válida antes da
     * execução. - O método `saidaProdutosNf()` deve estar implementado e
     * acessível.
     *
     * Observações: - O método não trata o caso em que nenhum resultado é
     * retornado — pode ser útil exibir uma mensagem ou limpar o estado. -
     * Recomenda-se usar `try-with-resources` para garantir o fechamento
     * automático da conexão e do `PreparedStatement`.
     */
    private void produto() {

        conexao = ModuloConexao.conector();

        String sql = "select nfCod from produ_entrada where nfProduto = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nf.toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                cod.append(rs.getString(1));
            }
            saidaProdutosNf();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método saidaProdutosNf.
     *
     * Carrega os dados detalhados do produto vinculado à nota fiscal
     * selecionada.
     *
     * Este método realiza uma consulta ao banco de dados para recuperar
     * informações completas sobre o produto, fornecedor, nota fiscal, validade,
     * tipo, setor, unidade de medida, descrição e quantidade disponível. Os
     * dados são exibidos nos campos da interface gráfica.
     *
     * Ações realizadas: - Executa a query: ```sql SELECT cp.nomeProduto,
     * pe.nomeFornecedor, pe.nfProduto, pe.dataValid, cp.tipoProduto,
     * cp.setorProduto, cp.unidadeMedida, cp.descProduto, pe.quantidadeTotal
     * FROM produ_entrada pe JOIN cadastro_prod cp ON pe.cod = cp.cod WHERE
     * pe.nfCod = ? ``` - Preenche os campos da interface com os dados
     * retornados: - `txaRetProdNome`: nome do produto - `txaRetProdForn`: nome
     * do fornecedor - `txaRetProdNf`: número da nota fiscal -
     * `txtRetProdValidade`: data de validade - `txtRetProdTipo`: tipo do
     * produto - `txtRetProdSetor`: setor do produto - `txtRetProdMed`: unidade
     * de medida - `txaRetProdDesc`: descrição do produto - `txtRetProdQuantNf`:
     * quantidade disponível - Define os valores de `limite` e `total` com base
     * na quantidade disponível. - Configura o spinner `spnProdRetirar` com
     * intervalo de 0 até o limite. - Fecha a conexão após o uso.
     *
     * Requisitos: - O buffer `cod` deve conter o código interno da nota fiscal
     * (`nfCod`). - Os componentes da interface devem estar inicializados.
     *
     * Observações: - O método assume que haverá resultado na consulta —
     * recomenda-se tratar o caso em que `rs.next()` retorna `false`. - O uso de
     * `SpinnerNumberModel` permite limitar a quantidade de retirada com base no
     * estoque disponível.
     */
    private void saidaProdutosNf() {

        String sql = "select cp.nomeProduto, pe.nomeFornecedor, pe.nfProduto, pe.dataValid, cp.tipoProduto, cp.setorProduto, cp.unidadeMedida, cp.descProduto, pe.quantidadeTotal from produ_entrada pe join cadastro_prod cp on pe.cod = cp.cod  where pe.nfCod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cod.toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                txaRetProdNome.setText(rs.getString(1));
                txaRetProdForn.setText(rs.getString(2));
                txaRetProdNf.setText(rs.getString(3));
                txtRetProdValidade.setText(rs.getString(4));
                txtRetProdTipo.setText(rs.getString(5));
                txtRetProdSetor.setText(rs.getString(6));
                txtRetProdMed.setText(rs.getString(7));
                txaRetProdDesc.setText(rs.getString(8));
                txtRetProdQuantNf.setText(rs.getString(9));
                limite = (rs.getInt(9));
                total = (rs.getInt(9));
                spnProdRetirar.setModel(new SpinnerNumberModel(0, 0, limite, 1));

                conexao.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método calculosRetirada.
     *
     * Realiza o cálculo da quantidade restante de produto após a retirada.
     *
     * Este método obtém o valor selecionado no componente `spnProdRetirar`, que
     * representa a quantidade de produto a ser retirada, e subtrai esse valor
     * do total disponível (`total`). O resultado é exibido no campo
     * `txtRetProdQuantNf`, representando a nova quantidade restante.
     *
     * Ações realizadas: - Obtém o valor atual do spinner `spnProdRetirar` e
     * armazena em `retirada`. - Calcula `calculo = total - retirada`. -
     * Atualiza o campo `txtRetProdQuantNf` com o valor calculado.
     *
     * Requisitos: - O spinner `spnProdRetirar` deve estar corretamente
     * configurado com limites válidos. - A variável `total` deve conter a
     * quantidade atual disponível antes da retirada. - O campo
     * `txtRetProdQuantNf` deve estar inicializado.
     *
     * Observações: - O método não valida se `retirada` excede `total`, pois o
     * spinner já limita esse valor. - Pode ser útil adicionar feedback visual
     * ou sonoro ao usuário após o cálculo.
     */
    private void calculosRetirada() {

        retirada = (int) spnProdRetirar.getValue();
        calculo = total - retirada;
        txtRetProdQuantNf.setText(String.valueOf(calculo));
    }

    /**
     * Método verificação.
     *
     * Verifica se há uma quantidade válida de produto a ser retirada e habilita
     * ou desabilita o botão de atualização.
     *
     * Este método obtém o valor atual do spinner `spnProdRetirar`, que
     * representa a quantidade de retirada, e habilita o botão
     * `btnProdSaiAtuEstoq` apenas se esse valor for maior que zero. Caso
     * contrário, o botão é desabilitado para evitar ações inválidas.
     *
     * Ações realizadas: - Obtém o valor do spinner como inteiro (`verif`). - Se
     * `verif > 0`, habilita o botão de atualização de estoque. - Caso
     * contrário, desabilita o botão.
     *
     * Requisitos: - O componente `spnProdRetirar` deve estar corretamente
     * configurado. - O botão `btnProdSaiAtuEstoq` deve estar inicializado.
     *
     * Observações: - Este método pode ser chamado após alterações no spinner
     * para garantir que o botão reflita o estado atual. - Pode ser útil
     * adicionar feedback visual ao usuário quando o botão estiver desabilitado.
     */
    private void verificação() {

        int verif;

        verif = (int) spnProdRetirar.getValue();

        if (verif > 0) {
            btnProdSaiAtuEstoq.setEnabled(true);
        } else {
            btnProdSaiAtuEstoq.setEnabled(false);
        }

    }

    /**
     * Método modifiacarProdutos.
     *
     * Atualiza a quantidade de um produto no banco de dados após a retirada.
     *
     * Este método calcula a nova quantidade total de um produto com base na
     * quantidade retirada informada pelo usuário e atualiza o registro
     * correspondente na tabela `cadastro_prod`. Se a operação for bem-sucedida,
     * chama o método `atualizacaoEntradaProd()` para continuar o fluxo.
     *
     * Ações realizadas: - Obtém a quantidade total atual do produto
     * (`total_pro`) a partir do campo `txtRetProdQuntTotal`. - Obtém a
     * quantidade a ser retirada (`total_nf`) do spinner `spnProdRetirar`. -
     * Calcula a nova quantidade (`total_geral = total_pro - total_nf`). -
     * Executa a query: ```sql UPDATE cadastro_prod SET quantidadeProduto = ?
     * WHERE cod = ? ``` - Se a atualização for bem-sucedida (`adicionar > 0`),
     * chama `atualizacaoEntradaProd()`. - Caso contrário, exibe uma mensagem de
     * erro.
     *
     * Requisitos: - Os campos `txtRetProdQuntTotal` e `txtRetProdCod` devem
     * estar corretamente preenchidos. - O método `atualizacaoEntradaProd()`
     * deve estar implementado.
     *
     * Observações: - O nome do método contém um erro de digitação:
     * “modifiacarProdutos” deveria ser “modificarProdutos”. - Recomenda-se
     * validar os valores antes de executar a atualização para evitar
     * inconsistências. - O uso de `try-with-resources` pode melhorar o
     * gerenciamento da conexão.
     */
    private void modifiacarProdutos() {

        conexao = ModuloConexao.conector();

        int total_pro, total_nf, total_geral;

        total_pro = Integer.parseInt(txtRetProdQuntTotal.getText());
        total_nf = (int) spnProdRetirar.getValue();
        total_geral = total_pro - total_nf;

        String sql = "update cadastro_prod set quantidadeProduto = ? where cod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, total_geral);
            pst.setString(2, txtRetProdCod.getText());
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                atualizacaoEntradaProd();
            } else {
                JOptionPane.showMessageDialog(null, "Erro na Operação" + "\n" + "Verifique os Registros", "Atenção", 2);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método atualizacaoEntradaProd.
     *
     * Atualiza os dados de entrada do produto no banco de dados após a
     * retirada.
     *
     * Este método executa uma atualização na tabela produ_entrada, ajustando os
     * campos quantidadeSai e quantidadeTotal com base na quantidade retirada
     * informada pelo usuário. A operação é realizada para o produto
     * identificado por seu código e pelo código da nota fiscal (nfCod). Se a
     * atualização for bem-sucedida, o método saidaProdutos() é chamado para
     * continuar o fluxo de saída.
     *
     * Funcionalidade: - Obtém a quantidade retirada do componente
     * spnProdRetirar. - Obtém a nova quantidade restante do campo
     * txtRetProdQuantNf. - Executa a query: UPDATE produ_entrada SET
     * quantidadeSai = ?, quantidadeTotal = ? WHERE cod = ? AND nfCod = ? - Se a
     * atualização for bem-sucedida, chama saidaProdutos(). - Caso contrário,
     * exibe uma mensagem de erro. - Fecha a conexão com o banco de dados.
     *
     * Requisitos: - Os campos txtRetProdCod e txtRetProdQuantNf devem estar
     * preenchidos corretamente. - O buffer cod deve conter o valor de nfCod. -
     * O método saidaProdutos() deve estar implementado e acessível.
     *
     * Observações: - O método assume que a conexão já foi estabelecida
     * anteriormente.
     */
    private void atualizacaoEntradaProd() {

        String sql = "update produ_entrada set quantidadeSai = ?, quantidadeTotal = ? where cod = ? and nfCod = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, (int) spnProdRetirar.getValue());
            pst.setInt(2, Integer.parseInt(txtRetProdQuantNf.getText()));
            pst.setString(3, txtRetProdCod.getText());
            pst.setString(4, cod.toString());
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                saidaProdutos();
            } else {
                JOptionPane.showMessageDialog(null, "Erro na Operação" + "\n" + "Verifique os Registros", "Atenção", 2);
            }
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método saidaProdutos.
     *
     * Registra a saída de um produto no banco de dados.
     *
     * Este método insere um novo registro na tabela produ_saida, contendo
     * informações sobre a data e hora da saída, o usuário responsável, o código
     * do produto, a quantidade retirada e o código da nota fiscal (nfCod). Após
     * a inserção, exibe uma mensagem de sucesso, fecha a conexão, limpa os
     * campos da interface e recarrega os dados do setor.
     *
     * Funcionalidade: - Gera a data e hora atual formatada com estilo curto. -
     * Executa a query: INSERT INTO produ_saida (dataSai, idSai, cod,
     * quantidadeSaid, nfCod) VALUES (?, ?, ?, ?, ?) - Preenche os parâmetros
     * com os dados da interface e do sistema. - Se a inserção for bem-sucedida:
     * - Exibe mensagem de sucesso. - Fecha a conexão. - Chama os métodos
     * limpar() e setorAcesso().
     *
     * Requisitos: - Os campos txtRetProdCod e txtRetProdQuantNf devem estar
     * preenchidos corretamente. - O buffer cod deve conter o valor de nfCod. -
     * O método limpar() deve limpar os campos da interface. - O método
     * setorAcesso() deve recarregar os dados do setor atual.
     *
     * Observações: - O método utiliza ZonedDateTime para obter a data e hora
     * atual com fuso horário. - Recomenda-se utilizar try-with-resources para
     * melhor gerenciamento da conexão.
     */
    private void saidaProdutos() {

        StringBuilder format = new StringBuilder();

        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        format.append(data_hora.format(zdtNow));

        String sql = "insert into produ_saida (dataSai, idSai, cod, quantidadeSaid, nfCod) values (?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, format.toString());
            pst.setString(2, id);
            pst.setString(3, txtRetProdCod.getText());
            pst.setInt(4, Integer.parseInt(txtRetProdQuantNf.getText()));
            pst.setString(5, cod.toString());
            int adicionar = pst.executeUpdate();
            if (adicionar > 0) {
                JOptionPane.showMessageDialog(null, "Saida de Produtro Registrado com Sucesso");
                conexao.close();
                limpar();
                setorAcesso();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método limpar.
     *
     * Limpa todos os campos da interface gráfica relacionados à saída de
     * produtos.
     *
     * Este método redefine os campos de texto, spinner e botões da tela de
     * saída de produtos para seus valores iniciais. Também zera as variáveis
     * internas de controle e limpa a tabela de notas fiscais exibida na
     * interface.
     *
     * Funcionalidade: - Limpa os campos de texto relacionados ao produto,
     * fornecedor, nota fiscal, validade, tipo, setor, unidade de medida,
     * descrição e pesquisa. - Zera os campos de quantidade e código. - Reseta o
     * valor do spinner spnProdRetirar para 0 e o desabilita. - Desabilita os
     * botões btnProdSaiAtuEstoq e btnProdSaiNovo. - Zera as variáveis internas
     * retirada, calculo e total. - Chama o método calculosRetirada() para
     * atualizar os cálculos. - Limpa todas as linhas da tabela tblRetProdNf.
     *
     * Requisitos: - Todos os componentes da interface devem estar corretamente
     * inicializados. - O método calculosRetirada() deve estar implementado e
     * acessível.
     *
     * Observações: - Este método é útil após registrar uma saída ou ao iniciar
     * uma nova operação. - Pode ser chamado também ao cancelar uma ação ou ao
     * trocar de usuário/setor.
     */
    private void limpar() {

        txaRetProdNome.setText(null);
        txaRetProdForn.setText(null);
        txaRetProdNf.setText(null);
        txtRetProdValidade.setText(null);
        txtRetProdTipo.setText(null);
        txtRetProdSetor.setText(null);
        txtRetProdMed.setText(null);
        txaRetProdDesc.setText(null);
        txtRetProdPesqui.setText(null);
        txtRetProdQuantNf.setText("0");
        txtRetProdCod.setText("0");
        txtRetProdQuntTotal.setText("0");
        spnProdRetirar.setValue(0);
        spnProdRetirar.setEnabled(false);
        btnProdSaiAtuEstoq.setEnabled(false);
        btnProdSaiNovo.setEnabled(false);
        retirada = 0;
        calculo = 0;
        total = 0;
        calculosRetirada();
        ((DefaultTableModel) tblRetProdNf.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRetProd = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRetProdNf = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaRetProdNome = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        txtRetProdCod = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtRetProdTipo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaRetProdForn = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txaRetProdNf = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txaRetProdDesc = new javax.swing.JTextArea();
        txtRetProdSetor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtRetProdValidade = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtRetProdMed = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtRetProdQuantNf = new javax.swing.JTextField();
        txtRetProdQuntTotal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        spnProdRetirar = new javax.swing.JSpinner();
        txtRetProdPesqui = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnProdSaiNovo = new javax.swing.JButton();
        btnProdSaiAtuEstoq = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblAparencia = new JlabelGradient();

        jLabel2.setText("jLabel2");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Retirada de Produtos");
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
        jLabel1.setText("Retirada de Produtos ");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(276, 30, 458, 50);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblRetProd = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblRetProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblRetProd.getTableHeader().setResizingAllowed(false);
        tblRetProd.getTableHeader().setReorderingAllowed(false);
        tblRetProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRetProdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRetProd);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 126, 464, 186);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblRetProdNf = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblRetProdNf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nota Físcal ", "Validade"
            }
        ));
        tblRetProdNf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRetProdNfMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblRetProdNf);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(518, 126, 460, 186);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Produto");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(26, 390, 220, 20);

        txaRetProdNome.setEditable(false);
        txaRetProdNome.setColumns(20);
        txaRetProdNome.setLineWrap(true);
        txaRetProdNome.setRows(5);
        jScrollPane3.setViewportView(txaRetProdNome);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(26, 410, 220, 36);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        jLabel5.setText("Pesquisar");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(80, 95, 110, 28);

        txtRetProdCod.setEditable(false);
        txtRetProdCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRetProdCod.setText("0");
        getContentPane().add(txtRetProdCod);
        txtRetProdCod.setBounds(398, 346, 90, 28);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Fornecedor");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(269, 390, 220, 20);

        txtRetProdTipo.setEditable(false);
        txtRetProdTipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtRetProdTipo);
        txtRetProdTipo.setBounds(26, 480, 180, 28);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Tipo");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(26, 460, 180, 20);

        txaRetProdForn.setEditable(false);
        txaRetProdForn.setColumns(20);
        txaRetProdForn.setLineWrap(true);
        txaRetProdForn.setRows(5);
        jScrollPane4.setViewportView(txaRetProdForn);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(269, 410, 220, 36);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Nota Fiscal");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(511, 390, 220, 20);

        txaRetProdNf.setEditable(false);
        txaRetProdNf.setColumns(20);
        txaRetProdNf.setLineWrap(true);
        txaRetProdNf.setRows(5);
        jScrollPane5.setViewportView(txaRetProdNf);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(511, 410, 220, 36);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Descrição");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(754, 390, 220, 20);

        txaRetProdDesc.setEditable(false);
        txaRetProdDesc.setColumns(20);
        txaRetProdDesc.setLineWrap(true);
        txaRetProdDesc.setRows(5);
        jScrollPane6.setViewportView(txaRetProdDesc);

        getContentPane().add(jScrollPane6);
        jScrollPane6.setBounds(754, 410, 220, 36);

        txtRetProdSetor.setEditable(false);
        txtRetProdSetor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtRetProdSetor);
        txtRetProdSetor.setBounds(226, 480, 180, 28);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Setor");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(226, 460, 180, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Validade");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(426, 460, 100, 20);

        txtRetProdValidade.setEditable(false);
        txtRetProdValidade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtRetProdValidade);
        txtRetProdValidade.setBounds(426, 480, 100, 28);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Medida");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(546, 460, 100, 20);

        txtRetProdMed.setEditable(false);
        txtRetProdMed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtRetProdMed);
        txtRetProdMed.setBounds(546, 480, 100, 28);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Quantida NF");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(666, 460, 90, 20);

        txtRetProdQuantNf.setEditable(false);
        txtRetProdQuantNf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRetProdQuantNf.setText("0");
        getContentPane().add(txtRetProdQuantNf);
        txtRetProdQuantNf.setBounds(666, 480, 90, 28);

        txtRetProdQuntTotal.setEditable(false);
        txtRetProdQuntTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRetProdQuntTotal.setText("0");
        getContentPane().add(txtRetProdQuntTotal);
        txtRetProdQuntTotal.setBounds(512, 346, 90, 28);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Quantida ");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(512, 326, 90, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Retirar");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(778, 460, 90, 20);

        spnProdRetirar.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9999999, 1));
        spnProdRetirar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnProdRetirarStateChanged(evt);
            }
        });
        getContentPane().add(spnProdRetirar);
        spnProdRetirar.setBounds(778, 480, 90, 28);

        txtRetProdPesqui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRetProdPesquiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRetProdPesquiKeyTyped(evt);
            }
        });
        getContentPane().add(txtRetProdPesqui);
        txtRetProdPesqui.setBounds(190, 95, 200, 28);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Código");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(398, 326, 90, 20);

        btnProdSaiNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48760_file_file.png"))); // NOI18N
        btnProdSaiNovo.setToolTipText("Nova Consulta");
        btnProdSaiNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdSaiNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdSaiNovo);
        btnProdSaiNovo.setBounds(782, 534, 64, 64);

        btnProdSaiAtuEstoq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Atualizar1.png"))); // NOI18N
        btnProdSaiAtuEstoq.setToolTipText("Retirar/Atualizar");
        btnProdSaiAtuEstoq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdSaiAtuEstoqActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdSaiAtuEstoq);
        btnProdSaiAtuEstoq.setBounds(877, 534, 64, 64);

        jLabel17.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(jLabel17);
        jLabel17.setBounds(750, 526, 220, 84);

        jLabel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 90, 978, 540);
        getContentPane().add(lblAparencia);
        lblAparencia.setBounds(0, 0, 1000, 640);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened.
     *
     * Executado automaticamente quando o frame interno é aberto.
     *
     * Este método é chamado quando o JInternalFrame é exibido pela primeira
     * vez. Ele realiza a configuração inicial da interface, como ajuste das
     * tabelas, definição de renderizadores, modo de seleção e restrições de
     * entrada nos componentes.
     *
     * Funcionalidade: - Chama ajustarTabela() para aplicar formatação visual às
     * tabelas. - Chama setorAcesso() para carregar os dados do setor do
     * usuário. - Define renderizadores personalizados para as tabelas
     * tblRetProd e tblRetProdNf. - Define o modo de seleção das tabelas como
     * seleção única. - Configura o spinner spnProdRetirar para aceitar apenas
     * valores numéricos válidos. - Desabilita os botões btnProdSaiAtuEstoq e
     * btnProdSaiNovo. - Desabilita o spinner spnProdRetirar. - Limpa a tabela
     * tblRetProdNf.
     *
     * Requisitos: - Os componentes da interface devem estar inicializados
     * corretamente. - Os métodos ajustarTabela() e setorAcesso() devem estar
     * implementados.
     *
     * Observações: - Este método é vinculado ao evento InternalFrameEvent e é
     * útil para preparar a interface antes da interação do usuário.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        ajustarTabela();
        setorAcesso();
        tblRetProd.setDefaultRenderer(Object.class, new alinhar());
        tblRetProdNf.setDefaultRenderer(Object.class, new alinharTabNf());
        tblRetProd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRetProdNf.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ((DefaultFormatter) ((JSpinner.NumberEditor) spnProdRetirar.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false); //impede que se digite qualquer caracter não-numérico no spinner
        btnProdSaiAtuEstoq.setEnabled(false);
        btnProdSaiNovo.setEnabled(false);
        spnProdRetirar.setEnabled(false);
        ((DefaultTableModel) tblRetProdNf.getModel()).setRowCount(0);
    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método txaProdFornecedorKeyTyped.
     *
     * Controla os caracteres digitados no campo de texto `txaProdFornecedor`,
     * permitindo apenas letras (com acentuação), números e símbolos
     * específicos. Caso o caractere digitado não esteja na lista permitida, o
     * evento é consumido, impedindo sua inserção no campo.
     *
     * Caracteres permitidos: - Letras: A–Z (maiúsculas e minúsculas), incluindo
     * acentos como Á, Ã, Ç, etc. - Números: 0–9 - Símbolos: - + _ ( ) / , ; . &
     *
     * @ e espaço
     *
     * Utilizado para garantir que o nome do fornecedor seja inserido com
     * formatação válida.
     *
     * @param evt Evento de tecla gerado quando o usuário digita no campo.
     */
    private void txtRetProdPesquiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetProdPesquiKeyTyped

        String caracter = "AaÃãÁáÀÀBbCcÇçDdEeÉéèÉÊêFfGgHhIiÍíÌìJjKkLlMmNnOoõÓóÒòÔôPpQqRrSsTtUuVvWwXxYyZz-+_()/0123456789 ,;.&@";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtRetProdPesquiKeyTyped

    /**
     * Método spnProdRetirarStateChanged.
     *
     * Evento disparado quando o valor do spinner spnProdRetirar é alterado.
     *
     * Este método é chamado automaticamente sempre que o usuário modifica o
     * valor do spinner responsável pela quantidade de produtos a serem
     * retirados. Ele atualiza os cálculos da quantidade restante e verifica se
     * o botão de atualização de estoque deve ser habilitado.
     *
     * Funcionalidade: - Chama o método calculosRetirada() para recalcular a
     * quantidade restante. - Chama o método verificação() para habilitar ou
     * desabilitar o botão de atualização.
     *
     * Requisitos: - Os métodos calculosRetirada() e verificação() devem estar
     * implementados e acessíveis. - O componente spnProdRetirar deve estar
     * corretamente configurado com valores válidos.
     *
     * Observações: - Este método está vinculado ao evento ChangeEvent do
     * spinner. - Ideal para garantir que a interface reaja dinamicamente às
     * ações do usuário.
     */
    private void spnProdRetirarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnProdRetirarStateChanged

        calculosRetirada();
        verificação();
    }//GEN-LAST:event_spnProdRetirarStateChanged

    /**
     * Método tblRetProdMouseClicked.
     *
     * Evento disparado ao clicar em uma linha da tabela de produtos.
     *
     * Este método é chamado automaticamente quando o usuário clica em uma linha
     * da tabela tblRetProd. Ele limpa os campos da interface, define os dados
     * do produto selecionado e atualiza a tabela de notas fiscais com base na
     * pesquisa atual.
     *
     * Funcionalidade: - Chama o método limpar() para redefinir os campos da
     * interface. - Chama o método setProd() para preencher os campos com os
     * dados do produto selecionado. - Verifica se o campo de pesquisa
     * txtRetProdPesqui está vazio. - Se estiver vazio, chama setorAcesso() para
     * carregar os dados do setor. - Caso contrário, chama
     * tabelaProdInicioPesq() para aplicar o filtro da pesquisa.
     *
     * Requisitos: - A tabela tblRetProd deve conter dados e permitir seleção de
     * linha. - Os métodos limpar(), setProd(), setorAcesso() e
     * tabelaProdInicioPesq() devem estar implementados.
     *
     * Observações: - Este método está vinculado ao evento MouseEvent da tabela.
     * - Ideal para garantir que a interface reaja dinamicamente à seleção de
     * produtos.
     */
    private void tblRetProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRetProdMouseClicked

        limpar();
        setProd();
        String str;
        str = txtRetProdPesqui.getText();

        if (str == null) {
            setorAcesso();
        } else {
            tabelaProdInicioPesq();
        }
    }//GEN-LAST:event_tblRetProdMouseClicked

    /**
     * Método tblRetProdNfMouseClicked
     *
     * Disparado ao clicar em uma linha da tabela de notas fiscais de produtos.
     *
     * Este método executa as seguintes ações: - Chama setProdNf() para carregar
     * os dados da nota fiscal do produto selecionado. - Habilita o botão
     * btnProdSaiNovo, permitindo iniciar uma nova saída de produto. - Habilita
     * o componente spnProdRetirar, permitindo definir a quantidade a ser
     * retirada.
     *
     * Requisitos: - A tabela tblRetProdNf deve conter dados válidos. - Os
     * componentes btnProdSaiNovo e spnProdRetirar devem estar inicializados.
     *
     * Observações: - Este método está vinculado ao evento MouseEvent da tabela
     * tblRetProdNf.
     */
    private void tblRetProdNfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRetProdNfMouseClicked

        setProdNf();
        btnProdSaiNovo.setEnabled(true);
        spnProdRetirar.setEnabled(true);
    }//GEN-LAST:event_tblRetProdNfMouseClicked

    /**
     * Método btnProdSaiAtuEstoqActionPerformed
     *
     * Disparado quando o botão btnProdSaiAtuEstoq é clicado.
     *
     * Este método chama modifiacarProdutos(), que realiza a atualização da
     * quantidade de produtos no estoque após a retirada informada pelo usuário.
     *
     * Funcionalidade: - Executa a lógica de modificação do estoque com base na
     * quantidade retirada.
     *
     * Requisitos: - O botão btnProdSaiAtuEstoq deve estar habilitado. - O
     * método modifiacarProdutos() deve estar implementado e acessível.
     *
     * Observações: - Este método está vinculado ao evento ActionEvent do botão.
     */
    private void btnProdSaiAtuEstoqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdSaiAtuEstoqActionPerformed

        modifiacarProdutos();
    }//GEN-LAST:event_btnProdSaiAtuEstoqActionPerformed

    /**
     * Método btnProdSaiNovoActionPerformed
     *
     * Disparado quando o botão "Novo" da tela de saída de produtos é clicado.
     *
     * Este método realiza a limpeza dos campos da interface gráfica
     * relacionados à retirada de produtos, preparando o formulário para uma
     * nova operação.
     *
     * Funcionalidade: - Chama o método limpar() para resetar os campos de
     * entrada. - Remove todas as linhas da tabela tblRetProdNf. - Limpa os
     * campos txtRetProdCod e txtRetProdQuntTotal. - Chama setorAcesso() para
     * configurar permissões ou visibilidade de componentes.
     *
     * Requisitos: - A tabela tblRetProdNf deve estar inicializada com um modelo
     * DefaultTableModel. - Os campos txtRetProdCod e txtRetProdQuntTotal devem
     * estar acessíveis. - O método setorAcesso() deve estar implementado
     * corretamente.
     *
     * Observações: - Este método é vinculado ao evento ActionEvent do botão
     * "Novo".
     */
    private void btnProdSaiNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdSaiNovoActionPerformed

        limpar();
        ((DefaultTableModel) tblRetProdNf.getModel()).setRowCount(0);
        txtRetProdCod.setText(null);
        txtRetProdQuntTotal.setText(null);
        setorAcesso();
    }//GEN-LAST:event_btnProdSaiNovoActionPerformed

    /**
     * Método txtRetProdPesquiKeyReleased
     *
     * Disparado quando uma tecla é liberada no campo de texto txtRetProdPesqui.
     *
     * Este método chama tabelaProdInicioPesq(), que provavelmente atualiza a
     * tabela de produtos com base no texto digitado, permitindo uma busca
     * dinâmica.
     *
     * Funcionalidade: - Detecta a liberação de uma tecla no campo de pesquisa.
     * - Atualiza a exibição da tabela de produtos conforme o texto digitado.
     *
     * Requisitos: - O campo txtRetProdPesqui deve estar vinculado a um
     * KeyListener. - O método tabelaProdInicioPesq() deve estar implementado
     * corretamente.
     *
     * Observações: - Ideal para implementar busca em tempo real conforme o
     * usuário digita.
     */
    private void txtRetProdPesquiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetProdPesquiKeyReleased

        tabelaProdInicioPesq();

    }//GEN-LAST:event_txtRetProdPesquiKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProdSaiAtuEstoq;
    private javax.swing.JButton btnProdSaiNovo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblAparencia;
    private javax.swing.JSpinner spnProdRetirar;
    private javax.swing.JTable tblRetProd;
    private javax.swing.JTable tblRetProdNf;
    private javax.swing.JTextArea txaRetProdDesc;
    private javax.swing.JTextArea txaRetProdForn;
    private javax.swing.JTextArea txaRetProdNf;
    private javax.swing.JTextArea txaRetProdNome;
    private javax.swing.JTextField txtRetProdCod;
    private javax.swing.JTextField txtRetProdMed;
    private javax.swing.JTextField txtRetProdPesqui;
    private javax.swing.JTextField txtRetProdQuantNf;
    private javax.swing.JTextField txtRetProdQuntTotal;
    private javax.swing.JTextField txtRetProdSetor;
    private javax.swing.JTextField txtRetProdTipo;
    private javax.swing.JTextField txtRetProdValidade;
    // End of variables declaration//GEN-END:variables
}
