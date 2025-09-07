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

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Classe ImageRenderer1.
 *
 * Um renderizador personalizado para células de tabela (`JTable`) que exibem
 * imagens. Esta classe estende `DefaultTableCellRenderer` e substitui o método
 * `getTableCellRendererComponent` para renderizar objetos do tipo `ImageIcon`
 * redimensionados de acordo com o tamanho da célula.
 *
 * Funcionalidades: - Verifica se o valor da célula é uma instância de
 * `ImageIcon`. - Redimensiona a imagem para se ajustar à largura da coluna e à
 * altura da linha. - Cria um `JLabel` com a imagem redimensionada e o retorna
 * como componente da célula. - Caso o valor não seja uma imagem, delega a
 * renderização ao comportamento padrão.
 *
 * Exemplo de uso: ```java
 * table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer1());
 * ```
 *
 * Observações: - Ideal para tabelas que exibem miniaturas ou ícones. - Usa
 * `Image.SCALE_SMOOTH` para preservar a qualidade da imagem ao redimensionar.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class ImageRenderer1 extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            // Cast the value to an ImageIcon
            ImageIcon icon = (ImageIcon) value;

            // Create a new JLabel and set the icon
            JLabel label = new JLabel();
            label.setIcon(icon);

            // Get the cell size
            int width = table.getColumnModel().getColumn(column).getWidth();
            int height = table.getRowHeight(row);

            // Resize the image to fit the cell
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newimg);
            label.setIcon(newIcon);

            return label;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
