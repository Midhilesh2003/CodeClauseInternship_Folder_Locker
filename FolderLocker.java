import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

public class FolderLocker extends JFrame implements ActionListener {

    private JFileChooser fileChooser;
    private JCheckBox lockCheckBox;
    private JButton lockButton;

    public FolderLocker() {
        super("Folder Locker");
        fileChooser = new JFileChooser();
        lockCheckBox = new JCheckBox("Lock Folder");
        lockButton = new JButton("Lock/Unlock");
        lockButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(fileChooser);
        panel.add(lockCheckBox);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(lockButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lockButton) {
            lockOrUnlockFolder();
        }
    }

    private void lockOrUnlockFolder() {
        File selectedFolder = fileChooser.getSelectedFile();

        if (selectedFolder == null) {
            JOptionPane.showMessageDialog(this, "Please select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedFolder.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Please select a valid folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean lock = lockCheckBox.isSelected();
        boolean result = lockFolder(selectedFolder, lock);

        String action = lock ? "lock" : "unlock";
        String message = result ? "Folder " + action + "ed successfully." : "Failed to " + action + " the folder.";
        JOptionPane.showMessageDialog(this, message);
    }

    private boolean lockFolder(File folder, boolean lock) {
        try {
            Path path = folder.toPath();
            DosFileAttributeView dosFileAttributeView = Files.getFileAttributeView(path, DosFileAttributeView.class);

            if (lock) {
                dosFileAttributeView.setHidden(true);
            } else {
                dosFileAttributeView.setHidden(false);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FolderLocker());
    }
}
