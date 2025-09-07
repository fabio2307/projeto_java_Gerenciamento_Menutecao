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

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.prjmanutencao.dal.ModuloConexao;
import java.sql.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Classe `Arquivos`. Representa a interface gráfica para gerenciar arquivos
 * relacionados à aplicação. Contém variáveis para conexão com o banco de dados,
 * manipulação de arquivos, configuração de servidor remoto, integração com
 * webcam e armazenamento de imagens.
 *
 * Variáveis principais: - `Connection conexao`: Gerencia a conexão com o banco
 * de dados. - `PreparedStatement pst`: Permite a execução de comandos SQL
 * parametrizados. - `ResultSet rs`: Armazena os resultados de consultas SQL.
 *
 * Variáveis globais: - `regra`: Define regras específicas relacionadas à
 * funcionalidade. - `idOs` e `idArq`: Identificadores únicos de Ordem de
 * Serviço e Arquivo. - `BYTES_IMAGEM1`: Armazena imagens como arrays de bytes
 * para processamento e transferência.
 *
 * Variáveis de configuração: - `acesso`: Define o endereço de acesso (IP ou
 * local físico). - `porta`: Porta usada para comunicação (FTP ou outro
 * protocolo). - `log` e `sen`: Credenciais de login e senha para acesso ao
 * servidor ou banco de arquivos. - `dire`: Diretório remoto para armazenar
 * arquivos relacionados à aplicação.
 *
 * Funcionalidades: - Manipulação de arquivos, incluindo caminhos e nomes. -
 * Integração com webcam para captura e uso de imagens. - Configuração de
 * entrada e saída de dados no sistema de arquivos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class Arquivos extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public static String regra = "";
    public static String idOs = null;
    public static String idArq = null;

    private Dimension dimensao_defaul;
    private Webcam WEBCAM;
    boolean executando = false;
    String Imagem;
    public static byte[] BYTES_IMAGEM1 = null;

    String nom_arq = null;

    String acesso = ("127.0.0.1");
    int porta = 21;
    String log = "servidor";
    String sen = "12345";
    String dire = "arquivos_os";

    String fe1 = null;
    String filePath = null;
    public static String pathEnt = null;
    String pathSai = null;
    String nomeArq = null;
    public static String nomeFile = null;

    /**
     * Construtor da classe `Arquivos`. Este método inicializa os componentes da
     * interface gráfica e realiza configurações essenciais para o funcionamento
     * da classe ao ser instanciada.
     *
     * Funcionalidades: - `initComponents()`: Método gerado automaticamente pelo
     * NetBeans ou ferramentas semelhantes para configurar os elementos da
     * interface gráfica. - `conexao = ModuloConexao.conector()`: Estabelece uma
     * conexão com o banco de dados utilizando o método `conector` da classe
     * `ModuloConexao`, essencial para operações relacionadas ao banco. -
     * `setIcon()`: Método para definir o ícone da janela principal, melhorando
     * a apresentação visual da interface.
     *
     * Este construtor garante que a classe esteja totalmente inicializada e
     * pronta para interação com o usuário e o sistema.
     */
    public Arquivos() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    /**
     * Método estático `arquivos`. Responsável por habilitar ou desabilitar
     * botões na interface gráfica para gerenciar a ação de salvar arquivos.
     *
     * Funcionalidades: - `btnSalArq.setEnabled(true)`: Habilita o botão de
     * salvar arquivo, permitindo ao usuário realizar essa ação. -
     * `btnBaiArq.setEnabled(false)`: Desabilita o botão de baixar arquivo,
     * indicando que essa funcionalidade está indisponível no momento.
     *
     * Este método é útil para configurar o estado inicial ou para situações em
     * que apenas a opção de salvar arquivo está disponível.
     */
    static void arquivos() {

        btnSalArq.setEnabled(true);
        btnBaiArq.setEnabled(false);
    }

    /**
     * Método `arquivosSelec`. Configura os estados dos botões na interface
     * gráfica com base na seleção de um arquivo.
     *
     * Funcionalidades: - `btnWebCam.setEnabled(false)`: Desabilita o botão
     * relacionado à integração com webcam. - `btnSelArq.setEnabled(false)`:
     * Desabilita o botão para selecionar arquivos. -
     * `btnSalArq.setEnabled(false)`: Desabilita o botão para salvar arquivos. -
     * `btnBaiArq.setEnabled(true)`: Habilita o botão para baixar arquivos,
     * permitindo essa funcionalidade. - `btnApaArq.setEnabled(true)`: Habilita
     * o botão para apagar arquivos, indicando que o usuário pode realizar a
     * exclusão.
     *
     * Este método é chamado quando um arquivo específico é selecionado,
     * ajustando os botões disponíveis para refletir as ações possíveis.
     */
    private void arquivosSelec() {

        btnWebCam.setEnabled(false);
        btnSelArq.setEnabled(false);
        btnSalArq.setEnabled(false);
        btnBaiArq.setEnabled(true);
        btnApaArq.setEnabled(true);

    }

    /**
     * Método `limpar`. Responsável por redefinir os componentes da interface
     * gráfica à sua configuração inicial. Este método é utilizado para limpar
     * dados, desabilitar ações e restaurar elementos para o estado padrão.
     *
     * Funcionalidades: - `pathSai = null`: Remove qualquer referência ao
     * caminho de saída do arquivo. - `lblIma.setIcon(null)`: Redefine o ícone
     * exibido no label `lblIma`, deixando-o vazio. - Botões de ação: -
     * `btnSalArq.setEnabled(false)`: Desabilita o botão de salvar arquivos. -
     * `btnBaiArq.setEnabled(false)`: Desabilita o botão de baixar arquivos. -
     * `btnApaArq.setEnabled(false)`: Desabilita o botão de apagar arquivos. -
     * `btnWebCam.setEnabled(true)`: Habilita o botão relacionado à integração
     * com webcam. - `btnSelArq.setEnabled(true)`: Habilita o botão de seleção
     * de arquivos. - Campos de texto: - `txtCodArq.setText(null)`: Limpa o
     * campo de código do arquivo. - `txtNomeArq.setText(null)`: Limpa o campo
     * de nome do arquivo.
     *
     * Este método é útil para reinicializar a interface após a conclusão de uma
     * tarefa ou ao iniciar uma nova operação.
     */
    private void limpar() {
        pathSai = null;
        lblIma.setIcon(null);
        btnSalArq.setEnabled(false);
        btnBaiArq.setEnabled(false);
        btnApaArq.setEnabled(false);
        btnWebCam.setEnabled(true);
        btnSelArq.setEnabled(true);
        txtCodArq.setText(null);
        txtNomeArq.setText(null);
    }

    /**
     * Método `imagem`. Responsável por definir o ícone exibido no `lblIma` com
     * base no tipo de arquivo selecionado. Para arquivos como `pdf`, `docx` e
     * `txt`, são utilizados ícones padrão associados ao tipo. Para imagens
     * (`jpg` e `png`), o método ajusta dinamicamente o tamanho do ícone à
     * dimensão do `lblIma`. Caso ocorra uma exceção, o método exibe uma
     * mensagem de erro para o usuário.
     *
     * Funcionalidades: - Verificação do tipo de arquivo (`fe1`): - `pdf`,
     * `docx` e `txt`: Define ícones específicos utilizando o caminho dos
     * recursos. - `jpg` e `png`: Carrega a imagem diretamente do `filePath`,
     * ajusta o tamanho e define como ícone. - Tratamento de Exceções: -
     * Qualquer erro no processo de definição do ícone é capturado e exibido ao
     * usuário por meio de um `JOptionPane`.
     *
     * Fluxo do Método: - Realiza a verificação sequencial do tipo de arquivo. -
     * Ajusta dinamicamente o ícone para os tipos `jpg` e `png`, utilizando
     * `getScaledInstance` para redimensionamento. - Exibe uma mensagem de erro
     * em caso de falhas.
     */
    private void imagem() {

        try {
            if (fe1.equals("pdf")) {
                lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/pdf.png")));

            }
            if (fe1.equals("docx")) {
                lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/docx.png")));

            }
            if (fe1.equals("txt")) {
                lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/txt.png")));

            }
            if (fe1.equals("jpg")) {
                ImageIcon icon = new ImageIcon(pathEnt);
                icon.setImage(icon.getImage().getScaledInstance(lblIma.getWidth(), lblIma.getHeight(), Image.SCALE_SMOOTH));
                lblIma.setIcon(icon);
            }
            if (fe1.equals("png")) {
                ImageIcon icon = new ImageIcon(pathEnt);
                icon.setImage(icon.getImage().getScaledInstance(lblIma.getWidth(), lblIma.getHeight(), Image.SCALE_SMOOTH));
                lblIma.setIcon(icon);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisarArq`. Responsável por abrir um diálogo para seleção de
     * arquivos no sistema operacional. Permite ao usuário escolher arquivos com
     * extensões específicas e realiza ações com base no arquivo selecionado,
     * como exibir informações e configurar ícones na interface. Em caso de
     * erro, exibe uma mensagem de erro por meio de um `JOptionPane`.
     *
     * Funcionalidades: - **Configuração do JFileChooser**: - `file = new
     * JFileChooser()`: Inicializa o seletor de arquivos. - `filtro` e
     * `filtro1`: Criam filtros para limitar os tipos de arquivos que podem ser
     * selecionados: - `filtro`: Permite selecionar imagens nos formatos `JPEG`
     * e `PNG`. - `filtro1`: Permite selecionar arquivos nos formatos `TXT`,
     * `PDF` e `DOCX`. - `file.setCurrentDirectory(...)`: Define a pasta inicial
     * como a área de trabalho do usuário. - `file.setFileFilter(...)`: Aplica
     * os filtros ao seletor de arquivos. - `file.setDialogTitle(...)`: Define o
     * título do diálogo como "Arquivos". - **Ação ao Selecionar um Arquivo**: -
     * `pathEnt`: Recebe o caminho completo do arquivo selecionado. -
     * `txtNomeArq.setText(...)`: Atualiza o campo de texto com o nome do
     * arquivo sem a extensão. - `nomeArq`: Armazena o nome completo do arquivo
     * selecionado. - `verificarArquivo()`: Método chamado para realizar
     * validações ou ações relacionadas ao arquivo. - `fe1`: Identifica a
     * extensão do arquivo selecionado (ex.: `pdf`, `jpg`). - `imagem()`:
     * Configura o ícone para o tipo do arquivo. - `arquivos()`: Habilita ou
     * desabilita botões da interface. - **Tratamento de Exceções**: - Captura
     * `HeadlessException` e exibe uma mensagem ao usuário utilizando
     * `JOptionPane`.
     *
     * Fluxo do Método: - Inicializa e configura o `JFileChooser`. - Exibe o
     * diálogo para seleção de arquivos. - Verifica e processa o arquivo
     * selecionado (ex.: exibir ícone, habilitar botões). - Lida com erros de
     * maneira segura e informativa.
     */
    private void pesquisarArq() {
        try {

            file = new JFileChooser();
            filtro = new FileNameExtensionFilter("Imagem em JPEG e PNG", "jpg", "png");
            filtro1 = new FileNameExtensionFilter("Arquivo em TXT, PDF e DOCX", "txt", "pdf", "docx");
            file.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            file.setFileFilter(filtro);
            file.setFileFilter(filtro1);
            file.setFileFilter(filtro1);
            file.setDialogTitle("Arquivos");
            if (file.showOpenDialog(label1) == JFileChooser.APPROVE_OPTION) {
                pathEnt = file.getSelectedFile().getPath();
                txtNomeArq.setText(file.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));
                nomeArq = file.getSelectedFile().getName();
                verificarArquivo();
                int a = pathEnt.lastIndexOf('.');
                if (a >= 0) {
                    fe1 = pathEnt.substring(a + 1);
                    imagem();
                    arquivos();
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `baixarArquivo`. Responsável por permitir ao usuário salvar
     * arquivos no sistema local, utilizando um diálogo de seleção de arquivos.
     * Este método extrai informações do arquivo, aplica configurações ao nome e
     * caminho, e chama métodos auxiliares para realizar a transferência e
     * reinicializar componentes.
     *
     * Funcionalidades: - **Inicialização do JFileChooser**: - `file = new
     * JFileChooser()`: Inicializa o seletor de arquivos para salvar. -
     * `file.setDialogTitle("Salvar Arquivo")`: Define o título do diálogo como
     * "Salvar Arquivo". - `file.setFileSelectionMode(JFileChooser.FILES_ONLY)`:
     * Restringe a seleção apenas a arquivos. - `file.setSelectedFile(...)`:
     * Sugere um nome para o arquivo baseado no texto do campo `txtNomeArq`. -
     * **Ação ao Confirmar a Seleção**: - Verifica se o usuário aprovou a ação
     * no diálogo (`i == 0`). - Extrai e manipula a extensão do arquivo (`fe`)
     * usando `fileName.lastIndexOf('.')`. - Concatena o caminho e a extensão ao
     * nome do arquivo final (`pathSai`). - Cria uma instância do arquivo (`File
     * arquivo`) e ajusta o caminho completo. - Chama os métodos auxiliares: -
     * `receber()`: Provavelmente responsável por transferir o arquivo ao
     * destino especificado. - `limpar()`: Reinicializa componentes e variáveis
     * da interface gráfica. - **Log e Debug**: - `System.out.println(pathSai)`:
     * Exibe no console o caminho do arquivo para depuração.
     *
     * Fluxo do Método: - Exibe o diálogo para salvar arquivo. - Manipula as
     * informações do arquivo selecionado (nome, extensão, caminho). - Chama os
     * métodos auxiliares para realizar a transferência e reiniciar o estado da
     * interface.
     */
    private void baixarArquivo() {
        String fe = "";
        String fileName;
        fileName = pathSai;
        String nome_foto = (txtNomeArq.getText());

        file = new JFileChooser();
        file.setDialogTitle("Salvar Arquivo");
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file.setSelectedFile(new File(nome_foto.replaceFirst("[.][^.]+$", "")));
        int i = file.showSaveDialog(label1);
        if (i == 0) {
            int a = fileName.lastIndexOf('.');
            if (a >= 0) {
                fe = fileName.substring(a + 1);
                pathSai = (file.getSelectedFile().getName());
                File arquivo = file.getSelectedFile();
                pathSai = arquivo.getPath() + (".") + fe;
                receber();
                limpar();
            }
        }

    }

    /**
     * Método `comparar`. Realiza uma validação e processamento do caminho e
     * extensão de um arquivo, ajustando variáveis globais com as informações
     * relevantes. O método verifica se um caminho de entrada (`pathEnt`) foi
     * definido ou, caso contrário, utiliza um caminho alternativo (`path`) e
     * processa a extensão do arquivo.
     *
     * Funcionalidades: - **Verificação do Caminho de Entrada**: - Se `pathEnt`
     * não for nulo: - Atualiza a variável `nomeFile` com o texto obtido de
     * `txtNomeArq`. - Obtém a extensão do arquivo a partir do caminho
     * (`pathEnt`). - Se `pathEnt` for nulo: - Define `pathEnt` como o valor de
     * `path`. - Atualiza `nomeFile` e processa o caminho do arquivo. -
     * **Extração de Extensão**: - Localiza o último índice do ponto (`.`) no
     * nome do arquivo para determinar sua extensão. - A extensão é armazenada
     * na variável `fe1`. - **Exibição para Depuração**: - Exibe o caminho do
     * arquivo no console usando `System.out.println(pathEnt)`.
     *
     * Fluxo do Método: 1. Verifica se o caminho de entrada (`pathEnt`) é nulo.
     * 2. Atualiza variáveis relacionadas ao nome e ao caminho do arquivo. 3.
     * Determina a extensão do arquivo usando manipulação de strings. 4.
     * Armazena a extensão e exibe informações no console para depuração.
     */
    private void comparar() {

        if (pathEnt != (null)) {

            nomeFile = txtNomeArq.getText();
            String fileName = pathEnt;

            int a = fileName.lastIndexOf('.');
            if (a >= 0) {
                fe1 = fileName.substring(a + 1);

            }

        } else {
            nomeFile = txtNomeArq.getText();
            String fileName = pathEnt;

            int a = fileName.lastIndexOf('.');
            if (a >= 0) {
                fe1 = fileName.substring(a + 1);

            }

        }

    }

    /**
     * Método `inserirAquivoOs`. Este método insere informações de um arquivo no
     * banco de dados, associando-o a uma Ordem de Serviço (OS). Ele realiza
     * validações para garantir que os campos obrigatórios sejam preenchidos e
     * utiliza SQL para executar a operação. Em caso de sucesso na inserção,
     * chama o método `upload_file` para processar o arquivo. Caso ocorra algum
     * erro, uma mensagem apropriada é exibida ao usuário.
     *
     * Funcionalidades: - **Comparação Inicial**: - Chama o método `comparar()`
     * para garantir que as variáveis necessárias estejam configuradas
     * corretamente.
     *
     * - **Configuração da Query SQL**: - A string SQL insere dados na tabela
     * `arquivos_os` com os seguintes valores: - `nome_arq`: Nome completo do
     * arquivo, incluindo a extensão. - `tipo_arq`: Caminho completo do arquivo
     * no sistema. - `id_os`: ID da Ordem de Serviço associada.
     *
     * - **Validação de Campo Obrigatório**: - Verifica se o campo `txtNomeArq`
     * está vazio. - Exibe uma mensagem ao usuário se o campo obrigatório não
     * for preenchido.
     *
     * - **Execução da Inserção**: - Utiliza `pst.executeUpdate()` para executar
     * a query. - Verifica se a inserção foi bem-sucedida (`Adicionar > 0`). -
     * Caso tenha sucesso, chama o método `upload_file()` para realizar o
     * processamento do arquivo. - Caso falhe, exibe uma mensagem indicando erro
     * na inserção.
     *
     * - **Tratamento de Exceções**: - Captura erros relacionados à interface
     * gráfica (`HeadlessException`) e ao banco de dados (`SQLException`). -
     * Exibe uma mensagem amigável ao usuário informando o problema.
     */
    private void inserirAquivoOs() {

        comparar();

        String sql = "insert into arquivos_os( nome_arq, tipo_arq, id_os) values(?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeArq.getText() + (".") + fe1);
            pst.setString(2, acesso + nomeFile + (".") + fe1);
            txtNomeArq.setText(nomeFile + (".") + fe1);
            pst.setString(3, idOs);
            if (txtNomeArq.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o Campo Obrigatório!");

            } else {
                int Adicionar = pst.executeUpdate();
                if (Adicionar > 0) {
                    upload_file();

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Armazenar o Arquivo.");
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `selecionarArquivo`. Este método é responsável por pesquisar e
     * carregar informações de um arquivo associado a uma Ordem de Serviço (OS)
     * com base no seu ID (`idArq`) e no ID da OS (`idOs`). Realiza validações,
     * executa uma consulta SQL e atualiza os componentes da interface gráfica
     * com os dados obtidos. Em caso de erro ou falhas, exibe mensagens
     * apropriadas ao usuário.
     *
     * Funcionalidades: - **Preparação Inicial**: - Reseta o valor de `pathSai`
     * para `null`. - Atualiza o campo `txtCodArq` com o valor de `idArq`. -
     * Chama o método `arquivosSelec()` para ajustar o estado dos botões na
     * interface.
     *
     * - **Execução da Query SQL**: - Consulta na tabela `arquivos_os` os campos
     * `nome_arq` e `tipo_arq` com base nos IDs fornecidos. - `idArq`:
     * Identificador único do arquivo. - `idOs`: Identificador único da Ordem de
     * Serviço.
     *
     * - **Processamento de Resultados**: - Se a consulta retornar resultados
     * (`rs.next()`): - Atualiza o campo `txtNomeArq` com o nome do arquivo. -
     * Armazena o nome e o caminho do arquivo nas variáveis `nom_arq` e
     * `pathSai`. - Chama os métodos auxiliares: - `regra()`: Realiza ações
     * específicas baseadas em regras do sistema. - `subString()`: Manipula ou
     * ajusta o nome do arquivo. - `carregarArquivo()`: Executa o carregamento
     * do arquivo na interface ou no sistema. - Caso contrário: - Verifica se o
     * campo `txtCodArq` está vazio e exibe uma mensagem de erro indicando que o
     * ID é obrigatório. - Exibe uma mensagem indicando que não há arquivo
     * associado ao ID fornecido.
     *
     * - **Tratamento de Exceções**: - Captura erros relacionados ao ambiente
     * gráfico (`HeadlessException`) e ao banco de dados (`SQLException`). -
     * Exibe mensagens amigáveis ao usuário indicando o problema.
     */
    public void selecionarArquivo() {

        pathSai = null;
        txtCodArq.setText(idArq);
        arquivosSelec();

        String sql = "select  nome_arq, tipo_arq from arquivos_os where id_arq=? and id_os=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, idArq);
            pst.setString(2, idOs);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNomeArq.setText(rs.getString(1));
                nom_arq = (rs.getString(1));
                pathSai = (rs.getString(2));
                regra();
                subString();
                carregarArquivo();

            } else {
                if (txtCodArq.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Preencha o Campo Id para Realializar a Pesquisa!");

                } else {

                    JOptionPane.showMessageDialog(null, "Não a Arquivo com esse Id!");

                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Realiza a exclusão de um arquivo registrado no banco de dados.
     *
     * Exibe uma caixa de diálogo para confirmação do usuário. Se confirmado,
     * executa uma instrução SQL para remover o registro da tabela arquivos_os
     * com base no código informado no campo txtCodArq. Após a exclusão no
     * banco, chama o método apagarArquivo para remover o arquivo físico. Exibe
     * uma mensagem de sucesso ou trata possíveis exceções.
     */
    private void apagar() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Remover esse Arquivo?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from arquivos_os where id_arq=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCodArq.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    apagarArquivo();

                    JOptionPane.showMessageDialog(null, "Arquivo Deletado!");

                }

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método `verificarArquivo`. Este método verifica se um arquivo com um nome
     * específico já existe no servidor FTP. Ele estabelece uma conexão FTP,
     * autentica o usuário, muda o diretório de trabalho e lista os arquivos
     * existentes para realizar a comparação. Caso um arquivo com o mesmo nome
     * seja encontrado, exibe uma mensagem ao usuário, limpa os componentes da
     * interface e reabre o diálogo de pesquisa de arquivo.
     *
     * Funcionalidades: - **Estabelecimento de Conexão FTP**: -
     * `ftp.connect(acesso)`: Conecta ao servidor FTP utilizando o endereço IP
     * ou nome definido em `acesso`. - `ftp.login(log, sen)`: Autentica o
     * usuário com as credenciais de login e senha. -
     * `ftp.changeWorkingDirectory(dire + "/" + idOs)`: Altera o diretório de
     * trabalho para o diretório especificado, incluindo o ID da Ordem de
     * Serviço (`idOs`).
     *
     * - **Listagem de Arquivos**: - `ftp.listNames()`: Obtém uma lista dos
     * nomes dos arquivos/diretórios presentes no diretório de trabalho. - Itera
     * pela lista e compara o nome dos arquivos com `nomeArq`. - Caso o nome do
     * arquivo seja encontrado, define `igual = true`.
     *
     * - **Ação ao Encontrar Arquivo Duplicado**: - Exibe uma mensagem ao
     * usuário indicando que o arquivo já existe. - Chama os métodos: -
     * `limpar()`: Reinicia os componentes da interface gráfica. -
     * `pesquisarArq()`: Abre o diálogo de pesquisa de arquivos.
     *
     * - **Tratamento de Exceções**: - Captura e trata exceções de entrada/saída
     * (`IOException`) e erros relacionados à interface gráfica
     * (`HeadlessException`). - Exibe mensagens amigáveis ao usuário informando
     * o problema.
     */
    private void verificarArquivo() {

        boolean igual = false;
        try {

            FTPClient ftp = new FTPClient();
            //Fazendo a conexão
            ftp.connect(acesso);
            //Efetuando o Login
            ftp.login(log, sen);
            //Mundando o diretório de trabalho
            ftp.changeWorkingDirectory(dire + "/" + idOs);
            //Adiquirindo o nome dos arquivos / diretórios existentes
            String[] arq = ftp.listNames();
//            System.out.println("Listando arquivos: \n");
            for (String f : arq) {
                if (f.equals(nomeArq)) {
                    igual = true;
                }
            }

            if (igual == true) {
                JOptionPane.showMessageDialog(null, "Já Existe um Arquivo com este Nome Salvo");
                limpar();
                pesquisarArq();

            }
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `upload_file`. Responsável por realizar o envio de um arquivo para
     * um servidor FTP. Este método estabelece uma conexão com o servidor,
     * autentica o usuário, ajusta o diretório de trabalho e configura o modo de
     * transferência. Após o envio, exibe uma mensagem de sucesso ou erro ao
     * usuário.
     *
     * Funcionalidades: - **Preparação para Upload**: - `nomeFile =
     * txtNomeArq.getText()`: Obtém o nome do arquivo a ser enviado. -
     * Inicializa o cliente FTP (`FTPClient ftp = new FTPClient()`).
     *
     * - **Conexão e Configuração FTP**: - `ftp.connect(acesso, porta)`: Conecta
     * ao servidor FTP usando o endereço (`acesso`) e a porta definida
     * (`porta`). - `ftp.login(log, sen)`: Autentica o usuário com as
     * credenciais de login (`log`) e senha (`sen`). -
     * `ftp.changeWorkingDirectory(dire)`: Muda para o diretório de trabalho
     * definido em `dire`. - `ftp.makeDirectory(idOs)`: Cria um novo diretório
     * utilizando o ID da Ordem de Serviço (`idOs`). -
     * `ftp.changeWorkingDirectory(idOs)`: Muda para o diretório recém-criado. -
     * `ftp.enterLocalPassiveMode()`: Configura o modo de transferência como
     * passivo. - `ftp.setFileType(FTP.BINARY_FILE_TYPE)`: Define o tipo de
     * transferência como binário.
     *
     * - **Envio de Arquivo**: - Utiliza um `FileInputStream` para carregar o
     * arquivo localizado em `pathEnt`. - `ftp.storeFile(nomeFile, arqEnviar)`:
     * Transfere o arquivo para o servidor FTP. - Exibe mensagens ao usuário: -
     * Sucesso: "Arquivo Armazenado com Sucesso!" - Falha: Exibe um ícone
     * indicando erro e uma mensagem: "Erro ao Armazenar o Arquivo."
     *
     * - **Tratamento de Exceções**: - Captura erros de entrada/saída
     * (`IOException`) e exibe mensagens amigáveis ao usuário utilizando
     * `JOptionPane`.
     */
    private void upload_file() {
        nomeFile = txtNomeArq.getText();

        FTPClient ftp = new FTPClient();

        try {

            ftp.connect(acesso, porta);

            ftp.login(log, sen);

            ftp.makeDirectory(dire);
            ftp.changeWorkingDirectory(dire);
            ftp.makeDirectory(idOs);
            ftp.changeWorkingDirectory(idOs);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileInputStream arqEnviar = new FileInputStream(pathEnt)) {
                if (ftp.storeFile(nomeFile, arqEnviar)) {
                    JOptionPane.showMessageDialog(null, "Arquivo Armazenado com Sucesso!");

                } else {

                    lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/sem_aquivo.png")));
                    JOptionPane.showMessageDialog(null, "Erro ao Armazenar o Arquivo.");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método `subString`. Este método é utilizado para extrair a extensão de um
     * arquivo a partir de seu caminho completo (`pathSai`). Ele localiza o
     * último ponto (`.`) no nome do arquivo e utiliza o método `substring` para
     * obter a parte da string que representa a extensão do arquivo.
     *
     * Funcionalidades: - **Localização do Ponto**: -
     * `pathSai.lastIndexOf('.')`: Encontra a posição do último ponto no nome do
     * arquivo. - **Extração da Extensão**: - `pathSai.substring(a + 1)`: Obtém
     * a parte da string após o último ponto, que representa a extensão do
     * arquivo. - **Armazenamento**: - O valor extraído é armazenado na variável
     * `fe1`, representando a extensão do arquivo.
     *
     * Fluxo do Método: 1. Localiza o último ponto no caminho completo do
     * arquivo. 2. Verifica se o ponto existe (`a >= 0`). 3. Extrai e armazena a
     * extensão do arquivo na variável `fe1`.
     *
     * Pontos Importantes: - Este método depende de `pathSai` estar corretamente
     * configurado com o caminho completo do arquivo. - Caso o ponto (`.`) não
     * seja encontrado, a extensão não será extraída.
     */
    private void subString() {

        int a = pathSai.lastIndexOf('.');
        if (a >= 0) {
            fe1 = pathSai.substring(a + 1);
        }
    }

    /**
     * Método `carregarArquivo`. Este método é responsável por recuperar um
     * arquivo armazenado em um servidor FTP e processá-lo para exibição na
     * interface gráfica ou manipulação posterior. Ele estabelece uma conexão
     * FTP, autentica o usuário, configura o diretório de trabalho e transfere o
     * arquivo do servidor para a aplicação. Caso o arquivo seja recuperado com
     * sucesso, exibe o arquivo ou um ícone correspondente à sua extensão.
     *
     * Funcionalidades: - **Conexão ao Servidor FTP**: -
     * `ftpCliente.connect(acesso, porta)`: Conecta ao servidor FTP utilizando o
     * endereço e a porta definidos. - `ftpCliente.login(log, sen)`: Autentica o
     * usuário utilizando as credenciais de login e senha. -
     * `ftpCliente.enterLocalPassiveMode()`: Configura o modo de conexão como
     * passivo. - `ftpCliente.setFileType(FTP.BINARY_FILE_TYPE)`: Define o tipo
     * de transferência como binário.
     *
     * - **Configuração do Caminho do Arquivo**: -
     * `dire.concat("/").concat(idOs).concat("/").concat(nomeArquivo)`: Constrói
     * o caminho completo do arquivo no servidor FTP.
     *
     * - **Recuperação do Arquivo**: - Utiliza `ByteArrayOutputStream` para
     * armazenar o arquivo recuperado do servidor. -
     * `ftpCliente.retrieveFile(caminhoArquivo, outputStream)`: Transfere o
     * arquivo do servidor para o fluxo de saída.
     *
     * - **Exibição do Arquivo**: - Verifica a extensão do arquivo (`fe1`) para
     * determinar a ação: - Arquivos `pdf`, `docx`, `txt`: Exibe ícones
     * correspondentes na interface gráfica. - Arquivos `jpg`, `png`: Carrega e
     * redimensiona a imagem para exibição em `lblIma`. - Caso o arquivo esteja
     * corrompido ou a transferência falhe, exibe um ícone de erro e desativa o
     * botão de download.
     *
     * - **Desconexão do Servidor**: - `ftpCliente.logout()`: Efetua logout do
     * servidor FTP. - `ftpCliente.disconnect()`: Desconecta do servidor FTP.
     *
     * - **Tratamento de Exceções**: - Captura erros de entrada/saída
     * (`IOException`) e problemas relacionados à interface gráfica
     * (`HeadlessException`). - Exibe mensagens informativas ao usuário em caso
     * de falhas.
     *
     * Fluxo do Método: 1. Conecta ao servidor FTP e autentica o usuário. 2.
     * Configura o diretório de trabalho e transfere o arquivo. 3. Determina a
     * ação apropriada com base na extensão do arquivo. 4. Exibe mensagens ao
     * usuário indicando sucesso ou falha. 5. Finaliza a conexão com o servidor
     * FTP.
     */
    private void carregarArquivo() {

        String nomeArquivo = txtNomeArq.getText();

        FTPClient ftpCliente = new FTPClient();
        try {
            // Conecta ao servidor FTP
            ftpCliente.connect(acesso, porta);

            ftpCliente.login(log, sen);
            ftpCliente.enterLocalPassiveMode(); // Modo passivo
            ftpCliente.setFileType(FTP.BINARY_FILE_TYPE); // Tipo de arquivo binário

            // Arquivo que deseja recuperar
            String caminhoArquivo = dire.concat("/").concat(idOs).concat("/").concat(nomeArquivo);

            // Recupera o arquivo para um ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            boolean sucesso = ftpCliente.retrieveFile(caminhoArquivo, outputStream);

            if (sucesso) {

                // Cria um InputStream a partir do ByteArrayOutputStream
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                if (fe1.equals("pdf")) {

                    lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/pdf.png")));
                }
                if (fe1.equals("docx")) {

                    lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/docx.png")));
                }
                if (fe1.equals("txt")) {

                    lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/txt.png")));
                }
                // Aqui você pode usar o InputStream para exibir ou processar o arquivo sem salvá-lo
                // Por exemplo: carregar uma imagem em um componente de interface gráfica
                if (fe1.equals("jpg") || fe1.equals("png")) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(inputStream));
                    icon.setImage(icon.getImage().getScaledInstance(lblIma.getWidth(), lblIma.getHeight(), Image.SCALE_SMOOTH));
                    lblIma.setIcon(icon);
                    pathSai = caminhoArquivo;
                }

            } else {
                lblIma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/sem_aquivo.png")));
                JOptionPane.showMessageDialog(null, "Arquivo Corrompido!" + "\n" + "Falha ao Recuperar o Arquivo.", null, 2);
                btnBaiArq.setEnabled(false);
            }

            // Desconecta do servidor
            ftpCliente.logout();
            ftpCliente.disconnect();
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `receber`. Responsável por realizar o download de um arquivo do
     * servidor FTP para o sistema local. Este método estabelece a conexão FTP,
     * autentica o usuário, navega até o diretório de trabalho e transfere o
     * arquivo especificado para o local definido em `pathSai`. Após a
     * transferência, exibe mensagens de sucesso ou erro ao usuário.
     *
     * Funcionalidades: - **Conexão ao Servidor FTP**: - `ftp.connect(acesso,
     * porta)`: Conecta ao servidor FTP utilizando o endereço (`acesso`) e a
     * porta (`porta`). - `ftp.login(log, sen)`: Autentica o usuário utilizando
     * as credenciais de login e senha. - `ftp.changeWorkingDirectory(dire + "/"
     * + idOs)`: Navega para o diretório de trabalho associado à Ordem de
     * Serviço (`idOs`). - `ftp.setFileType(FTP.BINARY_FILE_TYPE)`: Define o
     * tipo de arquivo como binário para garantir a integridade na
     * transferência.
     *
     * - **Download do Arquivo**: - Cria um `FileOutputStream` para salvar o
     * arquivo no caminho especificado (`pathSai`). - `ftp.retrieveFile(nom_arq,
     * fos)`: Transfere o arquivo do servidor FTP para o `FileOutputStream`. -
     * Exibe mensagens ao usuário: - Sucesso: "Download Realizado!" - Falha:
     * "Erro ao Realizar o Download!"
     *
     * - **Controle de Botões**: - `btnBaiArq.setEnabled(true)`: Habilita o
     * botão de download em caso de sucesso. - `btnBaiArq.setEnabled(false)`:
     * Desabilita o botão de download em caso de falha.
     *
     * - **Encerramento de Recursos**: - `fos.close()`: Fecha o fluxo de saída
     * para evitar vazamentos de memória.
     *
     * - **Tratamento de Exceções**: - Captura erros de entrada/saída
     * (`IOException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Conecta ao servidor FTP e autentica o usuário. 2.
     * Navega até o diretório de trabalho e configura o tipo de arquivo. 3.
     * Transfere o arquivo e exibe mensagens de sucesso ou falha. 4. Finaliza a
     * conexão e encerra o fluxo de saída.
     */
    private void receber() {

        FTPClient ftp = new FTPClient();
        nom_arq = txtNomeArq.getText();

        try {

            ftp.connect(acesso, porta);

            ftp.login(log, sen);

            ftp.changeWorkingDirectory(dire + "/" + idOs);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            FileOutputStream fos = new FileOutputStream(pathSai);

            if (ftp.retrieveFile(nom_arq, fos)) {

                JOptionPane.showMessageDialog(null, "Download Realizado!", null, 1);
                btnBaiArq.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Realizar o Download!", null, 2);
                btnBaiArq.setEnabled(false);
            }
            fos.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `apagarArquivo`. Responsável por excluir um arquivo armazenado em
     * um servidor FTP. Este método conecta ao servidor FTP, autentica o
     * usuário, navega até o diretório de trabalho, e executa a operação de
     * exclusão do arquivo especificado pelo nome (`nom_arq`). Caso ocorra
     * alguma falha na operação, exibe uma mensagem de erro ao usuário.
     *
     * Funcionalidades: - **Conexão ao Servidor FTP**: - `ftp.connect(acesso,
     * porta)`: Conecta ao servidor FTP utilizando o endereço (`acesso`) e a
     * porta (`porta`). - `ftp.login(log, sen)`: Autentica o usuário com as
     * credenciais de login e senha. - `ftp.changeWorkingDirectory(dire + "/" +
     * idOs)`: Navega até o diretório de trabalho associado à Ordem de Serviço
     * (`idOs`).
     *
     * - **Configuração de Transferência**: - `ftp.enterLocalPassiveMode()`:
     * Configura o modo de conexão como passivo. -
     * `ftp.setFileType(FTP.BINARY_FILE_TYPE)`: Define o tipo de arquivo como
     * binário para garantir integridade.
     *
     * - **Exclusão do Arquivo**: - `ftp.deleteFile(nom_arq)`: Executa a
     * exclusão do arquivo especificado pelo nome (`nom_arq`).
     *
     * - **Desconexão do Servidor**: - `ftp.disconnect()`: Desconecta do
     * servidor FTP após a operação.
     *
     * - **Tratamento de Exceções**: - Captura erros de entrada/saída
     * (`IOException`) e exibe mensagens amigáveis ao usuário utilizando
     * `JOptionPane`.
     *
     * Fluxo do Método: 1. Conecta ao servidor FTP e autentica o usuário. 2.
     * Navega até o diretório de trabalho e configura o modo de conexão. 3.
     * Executa a exclusão do arquivo especificado. 4. Desconecta do servidor e
     * trata possíveis erros.
     */
    private void apagarArquivo() {

        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(acesso, porta);
            ftp.login(log, sen);

            ftp.changeWorkingDirectory(dire + "/" + idOs);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.deleteFile(nom_arq);
            ftp.disconnect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `iniciar_camera`. Este método inicializa e abre a webcam,
     * configura os elementos da interface gráfica relacionados à câmera e
     * inicia o fluxo de vídeo para exibição em tempo real no componente
     * associado. A execução ocorre em uma thread separada para garantir que a
     * interface gráfica permaneça responsiva.
     *
     * Funcionalidades: - **Execução em Thread Separada**: - Utiliza uma nova
     * thread para realizar as operações de inicialização da câmera, evitando
     * bloqueios na interface.
     *
     * - **Configuração Inicial da Câmera**: - Define `executando = true` para
     * indicar que a câmera está ativa. - Desabilita o botão de inicialização da
     * câmera (`btnIniCam`) para evitar múltiplas inicializações. - Exibe a
     * mensagem `"Iniciando..."` no componente `lblImaCam` enquanto a câmera é
     * configurada.
     *
     * - **Inicialização da Webcam**: - `Webcam.setDriver(new
     * WebcamDefaultDriver())`: Configura o driver padrão para a webcam. -
     * `WEBCAM = Webcam.getDefault()`: Seleciona a webcam padrão do sistema. -
     * `WEBCAM.open()`: Abre a câmera e aloca os recursos necessários para seu
     * funcionamento. - Limpa a mensagem em `lblImaCam` ao concluir a
     * inicialização.
     *
     * - **Início do Fluxo de Vídeo**: - Chama o método `Inicializa_video()`
     * para capturar e exibir o vídeo da câmera em tempo real.
     *
     * - **Atualização da Interface**: - Habilita os botões `btnCapCam`
     * (capturar imagem) e `btnParCam` (parar câmera) após a inicialização.
     *
     * Fluxo do Método: 1. Cria e executa uma thread separada para inicializar a
     * câmera. 2. Configura a câmera e os componentes da interface gráfica. 3.
     * Inicia o fluxo de vídeo. 4. Habilita os botões relevantes e exibe o
     * status adequado na interface.
     */
    private void iniciar_camera() {
        lblIma.setIcon(null);
        new Thread() {
            public void run() {
                executando = true;
                btnWebCam.setEnabled(false);
                lblIma.setText("Iniciando...");
                Webcam.setDriver(new WebcamDefaultDriver());
                WEBCAM = Webcam.getDefault();
                WEBCAM.open();
                lblIma.setText("");
                Inicializa_video();
                btnTirFoto.setEnabled(true);
                btnParCam.setEnabled(true);
            }
        }.start();
    }

    /**
     * Método `capturar_imagem`. Este método é responsável por capturar uma
     * imagem da câmera (via `WEBCAM`), redimensioná-la, convertê-la em bytes e
     * salvá-la no sistema local. Além disso, exibe a imagem capturada na
     * interface gráfica e ajusta os estados necessários.
     *
     * Funcionalidades: - **Captura de Imagem**: - Utiliza `WEBCAM.getImage()`
     * para capturar a imagem da câmera. - Converte a imagem capturada em bytes
     * utilizando `ByteArrayOutputStream`.
     *
     * - **Redimensionamento da Imagem**: - Redimensiona a imagem capturada para
     * dimensões pré-definidas (`720x480`) usando `Graphics2D` e armazena em um
     * novo `BufferedImage`.
     *
     * - **Geração de Nome Único**: - Usa um número aleatório (`Random`) para
     * gerar um nome único para o arquivo. - O nome gerado é definido no campo
     * de texto `txtNomeArq`.
     *
     * - **Salvamento do Arquivo**: - Garante que o diretório especificado
     * (`caminho`) exista. - Salva a imagem redimensionada em um arquivo com o
     * nome gerado, no diretório especificado.
     *
     * - **Conversão em Bytes**: - Converte a imagem redimensionada em bytes
     * utilizando `ByteArrayOutputStream`. - Armazena os bytes na variável
     * `BYTES_IMAGEM1` para uso posterior.
     *
     * - **Exibição na Interface**: - Cria um ícone com a imagem capturada e
     * redimensionada. - Ajusta o tamanho do ícone à dimensão do componente
     * `lblIma` e exibe o ícone.
     *
     * - **Encerramento e Ajustes**: - Fecha a câmera (`WEBCAM.close()`) e
     * atualiza o estado da aplicação. - Chama os métodos auxiliares
     * `arquivos()` e `parar_camera()` para finalizar e ajustar a interface.
     *
     * - **Tratamento de Exceções**: - Captura e trata erros de entrada/saída
     * (`IOException`) e exibe mensagens informativas ao usuário.
     *
     * Fluxo do Método: 1. Captura uma imagem da câmera. 2. Redimensiona e
     * armazena a imagem em um arquivo local. 3. Converte a imagem em bytes e
     * exibe na interface gráfica. 4. Finaliza a captura e ajusta os estados da
     * aplicação.
     */
    private void capturar_imagem() {

        String caminho = "/../systemOs"; // Caminho relativo
        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 720, Nova_Altura = 480; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtNomeArq.setText(Integer.toString(valor));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            pathEnt = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblIma.getWidth(), lblIma.getHeight(), Image.SCALE_SMOOTH));
            lblIma.setIcon(icon);
//
            WEBCAM.close();
            arquivos();
            parar_camera();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método `parar_camera`. Responsável por finalizar o uso da câmera,
     * encerrando sua execução e ajustando os estados da interface gráfica. Este
     * método utiliza uma nova thread para realizar as operações de maneira
     * assíncrona, evitando bloqueios na interface.
     *
     * Funcionalidades: - **Encerramento da Câmera**: - `executando = false`:
     * Define o estado de execução da câmera como falso, indicando que ela foi
     * desligada. - `WEBCAM.close()`: Fecha a câmera, liberando recursos
     * associados.
     *
     * - **Atualização da Interface**: - `btnWebCam.setEnabled(true)`: Habilita
     * o botão de inicialização da câmera, permitindo que ela seja reativada. -
     * `btnTirFoto.setEnabled(false)`: Desabilita o botão de captura de fotos,
     * já que a câmera está desligada. - `btnParCam.setEnabled(false)`:
     * Desabilita o botão de parar a câmera.
     *
     * - **Execução em Thread Separada**: - O método utiliza `new Thread()` para
     * executar as operações em uma thread separada. - Isso evita travamentos ou
     * lentidão na interface gráfica durante o encerramento da câmera.
     *
     * Fluxo do Método: 1. Cria e inicia uma thread para realizar o desligamento
     * da câmera. 2. Finaliza a execução da câmera e libera recursos. 3.
     * Atualiza os estados dos botões na interface gráfica.
     */
    private void parar_camera() {

        new Thread() {
            @Override
            public void run() {
                executando = false;
                WEBCAM.close();

                btnWebCam.setEnabled(true);
                btnTirFoto.setEnabled(false);
                btnParCam.setEnabled(false);
            }
        }.start();

    }

    /**
     * Método `encerrar`. Este método é responsável por fechar a janela ou
     * interface gráfica associada ao objeto atual. Ele utiliza o método
     * `dispose()` para liberar os recursos alocados pela janela, encerrando sua
     * exibição.
     *
     * Funcionalidades: - **Encerramento da Janela**: - `dispose()`: Remove a
     * janela da tela e libera os recursos relacionados a ela. - Este método é
     * útil para encerrar janelas específicas sem finalizar toda a aplicação.
     *
     * Fluxo do Método: 1. Executa o comando `dispose()` para encerrar a janela.
     * 2. Garante que os recursos alocados pela interface sejam liberados.
     */
    public void encerrar() {
        dispose();
    }

    /**
     * Método `regra`. Este método ajusta os estados dos botões na interface
     * gráfica com base no valor da variável `regra`. Caso a regra seja
     * "Encerrada", determinados botões relacionados à manipulação de arquivos e
     * à câmera são desativados, indicando que a funcionalidade está bloqueada
     * ou concluída. Caso contrário, os botões são habilitados para permitir
     * interação.
     *
     * Funcionalidades: - **Validação da Regra**: - Verifica se o valor da
     * variável `regra` é igual a "Encerrada". - Caso positivo: -
     * `btnWebCam.setEnabled(false)`: Desativa o botão relacionado à
     * inicialização da câmera. - `btnSelArq.setEnabled(false)`: Desativa o
     * botão para seleção de arquivos. - `btnApaArq.setEnabled(false)`: Desativa
     * o botão para apagar arquivos. - Caso contrário: -
     * `btnWebCam.setEnabled(true)`: Habilita o botão relacionado à
     * inicialização da câmera. - `btnSelArq.setEnabled(true)`: Habilita o botão
     * para seleção de arquivos.
     *
     * - **Depuração**: - `System.out.println(regra)`: Exibe o valor da variável
     * `regra` no console para facilitar a depuração.
     *
     * Fluxo do Método: 1. Verifica o valor da variável `regra`. 2. Ajusta os
     * estados dos botões na interface gráfica com base na regra. 3. Exibe o
     * valor da variável no console.
     */
    private void regra() {
        if (regra.equals("Encerrada")) {
            btnWebCam.setEnabled(false);
            btnSelArq.setEnabled(false);
            btnApaArq.setEnabled(false);
        } else {
            btnWebCam.setEnabled(true);
            btnSelArq.setEnabled(true);
        }
    }

    /**
     * Inicializa os componentes da interface gráfica.
     *
     * Este método configura os elementos visuais da janela, como botões, campos
     * de texto, tabelas, painéis e seus respectivos layouts, tamanhos, eventos
     * e propriedades. É chamado no construtor da classe para preparar a
     * interface antes da exibição.
     *
     * A anotação @SuppressWarnings("unchecked") é usada para suprimir avisos
     * relacionados a conversões não verificadas, geralmente em listas ou
     * tabelas.
     *
     * Este método é gerado automaticamente por ferramentas de desenvolvimento
     * visual e não deve ser modificado manualmente, a menos que necessário.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        setSize(545, 360);
        setTitle("Arquivos");
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setSize(270, 270);
        panel1.setLocation(20, 45);
        panel1.setBackground(new Color(255, 255, 255, 90));
        panel1.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 51, 90)));

        lblIma = new JLabel();
        lblIma.setLayout(null);
        lblIma.setSize(260, 260);
        lblIma.setLocation(5, 5);
        lblIma.setBackground(new Color(122, 125, 125));
        lblIma.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        lblIma.setHorizontalAlignment(JLabel.CENTER);
        lblIma.setForeground(new Color(153, 255, 0));
        lblIma.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblIma.setOpaque(true);

        label1 = new JLabel();

        label1.setText("Cód. Arquivo:");
        label1.setLayout(null);
        label1.setBounds(18, 9, 96, 28);
        label1.setAlignmentY(JLabel.CENTER);

        label1.setAlignmentX(JLabel.CENTER);

        label1.setForeground(new Color(0, 0, 51));
        label1.setFont(new Font("Tahoma", Font.BOLD, 14));

        label2 = new JLabel();

        label2.setText("Nome:");
        label2.setLayout(null);
        label2.setBounds(210, 9, 48, 28);
        label2.setHorizontalAlignment(JLabel.CENTER);

        label2.setVerticalAlignment(JLabel.CENTER);

        label2.setForeground(new Color(0, 0, 51));
        label2.setFont(new Font("Tahoma", Font.BOLD, 14));

        txtCodArq = new JTextField();

        txtCodArq.setBounds(114, 10, 90, 28);
        txtCodArq.setHorizontalAlignment(JLabel.CENTER);
        txtCodArq.setEditable(false);

        txtNomeArq = new JTextField();

        txtNomeArq.setBounds(258, 10, 260, 28);
        txtNomeArq.setHorizontalAlignment(JLabel.CENTER);
        txtNomeArq.setEditable(false);

        btnWebCam = new JButton();

        btnWebCam.setToolTipText("Iniciar WebCam");
        btnWebCam.setBounds(305, 75, 64, 64);
        btnWebCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48744_camera_camera.png")));
        btnWebCam.addActionListener((ActionEvent e) -> {

            Iniciar();
            iniciar_camera();
            limpar();
            btnWebCam.setEnabled(false);
        });

        btnTirFoto = new JButton();

        btnTirFoto.setToolTipText("Capturar");
        btnTirFoto.setBounds(379, 75, 64, 64);
        btnTirFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Captura.png")));
        btnTirFoto.setEnabled(false);
        btnTirFoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capturar_imagem();
            }

        });

        btnParCam = new JButton();

        btnParCam.setToolTipText("Parar");
        btnParCam.setBounds(453, 75, 64, 64);
        btnParCam.setEnabled(false);
        btnParCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Stop.png")));
        btnParCam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        executando = false;
                        WEBCAM.close();
                        lblIma.setIcon(null);
                        btnWebCam.setEnabled(true);
                        btnTirFoto.setEnabled(false);
                        btnParCam.setEnabled(false);
                    }
                }.start();

            }

        });

        btnSelArq = new JButton();

        btnSelArq.setToolTipText("Arquivos");
        btnSelArq.setBounds(305, 149, 64, 64);
        btnSelArq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48769_folder_empty_folder_empty.png")));
        btnSelArq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (executando != true) {
                    limpar();
                    pesquisarArq();
                } else {
                    new Thread() {
                        public void run() {
                            executando = false;
                            WEBCAM.close();
                            lblIma.setIcon(null);
                            btnWebCam.setEnabled(true);
                            btnTirFoto.setEnabled(false);
                            btnParCam.setEnabled(false);
                        }
                    }.start();

                    limpar();
                    pesquisarArq();

                }

            }

        });

        btnSalArq = new JButton();

        btnSalArq.setToolTipText("Salvar");
        btnSalArq.setBounds(379, 149, 64, 64);
        btnSalArq.setEnabled(false);
        btnSalArq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48753_diskette_diskette.png")));
        btnSalArq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirAquivoOs();
                limpar();

            }
        });

        btnBaiArq = new JButton();

        btnBaiArq.setToolTipText("Dowload");
        btnBaiArq.setBounds(453, 149, 64, 64);
        btnBaiArq.setEnabled(false);
        btnBaiArq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48773_download_load_download_load.png")));
        btnBaiArq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baixarArquivo();

            }
        });

        btnApaArq = new JButton();

        btnApaArq.setToolTipText("Apagar");
        btnApaArq.setBounds(379, 223, 64, 64);
        btnApaArq.setEnabled(false);
        btnApaArq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48756_delete_delete_document_document.png")));
        btnApaArq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apagar();
                limpar();

            }
        });
//      
        add(panel1);

        panel1.add(lblIma);

        add(label1);

        add(label2);

        add(txtCodArq);

        add(txtNomeArq);

        add(btnWebCam);

        add(btnSelArq);

        add(btnSalArq);

        add(btnBaiArq);

        add(btnApaArq);

        add(btnTirFoto);

        add(btnParCam);

    }

    /**
     * Método `formWindowClosing`. Este método é chamado automaticamente quando
     * a janela está prestes a ser fechada pelo usuário. Ele verifica se a
     * câmera está em execução e, caso positivo, chama o método `parar_camera()`
     * para desligá-la. Além disso, garante a limpeza de componentes e variáveis
     * da interface gráfica, prevenindo erros ou uso desnecessário de recursos.
     *
     * Funcionalidades: - **Verificação do Estado da Câmera**: - `if (executando
     * == true)`: Verifica se a variável `executando` indica que a câmera está
     * em operação. - Caso positivo, chama o método `parar_camera()` para
     * encerrar a câmera e liberar recursos.
     *
     * - **Limpeza da Interface**: - `limpar()`: Chamado independentemente do
     * estado da câmera. Este método redefine os componentes e variáveis para o
     * estado inicial.
     *
     * Fluxo do Método: 1. Verifica se a câmera está em execução. 2. Chama
     * `parar_camera()` para desligar a câmera, se necessário. 3. Chama
     * `limpar()` para garantir que os componentes e variáveis da interface
     * estejam limpos.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        if (executando == true) {
            parar_camera();
        }
        limpar();
    }

    private JLabel label1, label2;
    public static JLabel lblIma;
    private JTextField txtCodArq;
    public static JTextField txtNomeArq;
    private static JButton btnWebCam, btnSelArq, btnSalArq, btnBaiArq;
    private JButton btnApaArq, btnTirFoto, btnParCam;
    private JFileChooser file;
    private FileNameExtensionFilter filtro, filtro1;

    /**
     * Método `main`. Ponto de entrada principal para a execução da aplicação.
     * Este método utiliza a classe `java.awt.EventQueue` para inicializar a
     * interface gráfica na thread de despacho de eventos (Event Dispatch Thread
     * - EDT), garantindo que a interface seja criada e manipulada de forma
     * segura em um ambiente multitarefa.
     *
     * Funcionalidades: - **Inicialização do Programa**: - O método é declarado
     * como `public static void`, tornando-o acessível publicamente e permitindo
     * que o programa seja iniciado diretamente pelo interpretador Java.
     *
     * - **Execução da Interface Gráfica**: -
     * `java.awt.EventQueue.invokeLater(...)`: Garante que a instância da classe
     * `Arquivos` seja criada e exibida de forma segura na EDT. Isso evita
     * problemas relacionados ao acesso simultâneo a componentes da interface
     * gráfica em um ambiente de multithreading.
     *
     * - **Criação da Janela Principal**: - `new Arquivos().setVisible(true)`:
     * Cria uma nova instância da classe `Arquivos` (provavelmente uma `JFrame`)
     * e define sua visibilidade como verdadeira, exibindo a janela na tela.
     *
     * Fluxo do Método: 1. A JVM invoca o método `main` como o ponto de entrada
     * da aplicação. 2. Uma nova tarefa é enviada para a Event Dispatch Thread
     * utilizando `invokeLater`. 3. A interface gráfica é inicializada e exibida
     * ao usuário.
     */
    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Arquivos().setVisible(true);

            }
        });

    }

    /**
     * Método `Iniciar`. Este método inicializa a câmera do sistema, definindo
     * as dimensões padrão ou preferenciais, identificando as webcams
     * disponíveis e configurando a resolução desejada para a câmera
     * selecionada. Caso ocorra algum erro durante a inicialização, ele captura
     * a exceção e informa o usuário por meio de um diálogo.
     *
     * Funcionalidades: - **Configuração da Dimensão da Webcam**: -
     * `dimensao_defaul = WebcamResolution.VGA.getSize()`: Define a resolução
     * padrão da câmera como VGA. Opcionalmente, outra dimensão poderia ser
     * configurada.
     *
     * - **Seleção da Webcam**: - `List list = Webcam.getWebcams()`: Obtém a
     * lista de todas as webcams disponíveis. - Itera sobre as webcams na lista
     * e seleciona a última webcam encontrada, caso mais de uma esteja
     * conectada. - `WEBCAM.setViewSize(dimensao_defaul)`: Configura a dimensão
     * escolhida como o tamanho de exibição da webcam.
     *
     * - **Verificação de Resoluções Suportadas**: - Itera pelas resoluções
     * suportadas pela webcam (`WEBCAM.getViewSizes()`). - Permite que
     * resoluções disponíveis sejam listadas (comentário para fins de
     * depuração).
     *
     * - **Tratamento de Exceções**: - Captura `WebcamException` para lidar com
     * problemas na inicialização da câmera. - Exibe a mensagem de erro ao
     * usuário utilizando `JOptionPane`.
     *
     * Fluxo do Método: 1. Define as dimensões padrão ou preferenciais da
     * webcam. 2. Lista e seleciona uma webcam disponível. 3. Configura a
     * resolução desejada. 4. Opcionalmente, verifica e exibe as resoluções
     * suportadas. 5. Captura e trata erros, informando o usuário em caso de
     * falhas.
     */
    private void Iniciar() {
        try {
//            dimensao_defaul = new Dimension(176, 144); //UTILIZO A DIMENSÃO DE MINHA PREFERÊNCIA
            dimensao_defaul = WebcamResolution.VGA.getSize();//UTILIZA A DIMENSÃO PADRÃO DA WEBCAM
//            WEBCAM = Webcam.getDefault(); //UTILIZA A WEBCAM PADRÃO DO COMPUTADOR            
            List list = Webcam.getWebcams();
            for (int i = 0; i < list.size(); i++) {
                WEBCAM = (Webcam) list.get(i); //UTILIZA A ÚLTIMA WEBCAM ENCONTRADA, SE EXISTIR MAIS DE UMA PEGA A ÚLTIMA
            }
            WEBCAM.setViewSize(dimensao_defaul);

            for (Dimension dimensao : WEBCAM.getViewSizes()) { // AQUI EU CONSIGO SABER QUAIS SÃO RESOLUÇÕES SUPORTADA PELA WEBCAM
//                System.out.println("Largura: " + dimensao.getWidth() + "  Altura: " + dimensao.getHeight());
            }
//         
        } catch (WebcamException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    /**
     * Método `Inicializa_video`. Este método é responsável por exibir um fluxo
     * de vídeo ao vivo da câmera na interface gráfica, atualizando o componente
     * `lblIma` com a imagem capturada a cada ciclo. A execução ocorre dentro de
     * uma thread separada para garantir que a interface permaneça responsiva
     * enquanto o vídeo é atualizado.
     *
     * Funcionalidades: - **Execução em Thread Separada**: - Cria uma nova
     * thread para gerenciar o fluxo de vídeo da câmera. - Garante que a
     * interface gráfica não seja bloqueada durante a atualização contínua do
     * vídeo.
     *
     * - **Captura de Imagem**: - Utiliza `WEBCAM.getImage()` para capturar
     * frames da câmera ao vivo. - Cada frame é convertido em um objeto
     * `ImageIcon` para exibição na interface.
     *
     * - **Redimensionamento da Imagem**: - `icon.setImage(...)`: Ajusta o
     * tamanho da imagem capturada para se adequar às dimensões de `lblIma`. -
     * Garante que o vídeo seja exibido corretamente na interface gráfica.
     *
     * - **Atualização Contínua**: - O ciclo de captura e atualização ocorre em
     * um loop infinito enquanto a câmera está ativa (`executando`). - Utiliza
     * `Thread.sleep(50)` para definir um intervalo de 50 milissegundos entre
     * cada atualização, simulando um fluxo de vídeo contínuo.
     *
     * - **Tratamento de Exceções**: - Captura `InterruptedException` e exibe
     * mensagens amigáveis ao usuário por meio de `JOptionPane`.
     *
     * Fluxo do Método: 1. Cria e inicia uma thread para executar o fluxo de
     * vídeo. 2. Dentro da thread, captura frames da câmera continuamente
     * enquanto `executando` for verdadeiro. 3. Redimensiona e atualiza o
     * componente `lblIma` com cada frame capturado. 4. Finaliza o fluxo ao
     * ocorrer uma interrupção ou ao encerrar a execução.
     */
    private void Inicializa_video() {
        new Thread() {
            @Override
            public void run() {
                while (true && executando) {
                    try {
                        Image imagem = WEBCAM.getImage();
                        ImageIcon icon = new ImageIcon(imagem);
                        icon.setImage(icon.getImage().getScaledInstance(lblIma.getWidth(), lblIma.getHeight(), Image.SCALE_SMOOTH));
                        lblIma.setIcon(icon);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        JOptionPane.showConfirmDialog(null, e);
                    }
                }
            }
        }.start();
    }

    /**
     * Método `setIcon`. Este método define o ícone da janela principal da
     * aplicação utilizando a classe `Toolkit`. Ele carrega a imagem do ícone a
     * partir do caminho especificado nos recursos da aplicação e aplica ao
     * objeto `JFrame`, garantindo uma apresentação visual personalizada.
     *
     * Funcionalidades: - **Carregamento de Ícone**: -
     * `Toolkit.getDefaultToolkit().getImage(...)`: Utiliza a classe `Toolkit`
     * para carregar a imagem do ícone. - `getClass().getResource(...)`:
     * Especifica o caminho relativo para buscar o arquivo de imagem dentro dos
     * recursos da aplicação.
     *
     * - **Configuração do Ícone**: - `setIconImage(...)`: Aplica a imagem
     * carregada como ícone principal da janela da aplicação.
     *
     * Fluxo do Método: 1. Carrega a imagem do ícone usando o caminho
     * especificado. 2. Define a imagem como ícone principal da janela.
     */
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png")));
    }
}
