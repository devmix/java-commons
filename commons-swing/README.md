# commons-swing

Another MVC Swing toolkit with localization, two-way data binding, different LaF providers

## Examples

See `commons-swing-samples` module

```
public final class SampleDialog {

    private final View v;
    private final DialogDecorator dialog;

    private SampleDialog(final ToolkitService toolkit) {
        this.v = toolkit.createView();
        final Binder b = toolkit.createBinder();
        final SampleCtrl c = new SampleCtrl();
        final Verifiers iv = toolkit.verifiers();

        this.dialog = v.dialog().name("dialog")
                .a(v.panel().layout(new BorderLayout()).margin(8)
                        .a(v.list().name("users")
                                .listData(new User[]{
                                        new User("User1", "acc1", "user1"),
                                        new User("User2", "acc2", "user2"),
                                        new User("User3", "acc3", "user3")
                                }).preferredWidth(100).wrapScrollPane(), BorderLayout.WEST)

                        .a(v.panel().layout(new FormLayout(8, 8)).margin(0, 8, 0, 0)
                                .a(v.label().i18n("account"),
                                        v.textField().name("account").verifier(iv.blank()))
                                .a(v.label().i18n("Login"),
                                        v.textField().name("login").verifier(iv.blank()))
                                .a(v.label().i18n("Password"),
                                        v.textField().name("password").verifier(iv.blank())), BorderLayout.CENTER)

                        .a(v.panel().layout(new ToolbarLayout()).margin(8, 0, 0, 0)
                                .a(v.button().name("login-btn").i18n("Login")
                                        .onAction(this::onLoginBtn), ToolbarLayout.END)
                                .a(v.button().name("close-btn").i18n("Close")
                                        .onAction(this::onCloseBtn), ToolbarLayout.END)
                                .$(o -> {
                                    toolkit.utils().swing()
                                            .equalizeComponentsSize(v.name("login-btn").$(), v.name("close-btn").$());
                                }), BorderLayout.SOUTH));

        b.with(c, v)
                .bind("users").selectedElement().to(SampleCtrl::getSelectedUser)
                .bind("dialog").read().property("title").to(SampleCtrl::getTitle)
                .bind("login").read().to("selectedUser.login")
                .bind("account").read().to("selectedUser.account")
                .bind();

        dialog.$(o -> {
            o.setPreferredSize(new Dimension(400, 300));
            o.setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
            o.setResizable(false);
            o.pack();
            o.setLocationRelativeTo(null);
            o.setVisible(true);
        });
    }

    private void onLoginBtn(final ActionEvent e) {
        if (!VerifierStatus.VALID.equals(v.verifiers().verifyAll())) {
            JOptionPane.showMessageDialog(dialog.$(),
                    "Value of some fields is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            onCloseBtn(e);
        }
    }

    private void onCloseBtn(final ActionEvent e) {
        dialog.$().dispose();
    }

    public static void main(final String[] args) {
        WebLookAndFeel.setDecorateFrames(true);
        WebLookAndFeel.setDecorateDialogs(true);
        WebLookAndFeel.install();
        LanguageManager.install(new ResourceBundleDataSource("lang"), LanguageOptions.create()
                .enableFeature(LanguageOptions.Feature.RETURN_KEY_INSTEAD_EMPTY_VALUE));
        new SampleDialog(new WebLaFToolkitService());
    }

    public static final class User {
        private final String name;
        private final String account;
        private final String login;

        public User(final String name, final String account, final String login) {
            this.name = name;
            this.account = account;
            this.login = login;
        }

        public String getName() {
            return name;
        }

        public String getLogin() {
            return login;
        }

        public String getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class SampleCtrl extends AbstractController {
        private User selectedUser;

        public User getSelectedUser() {
            return selectedUser;
        }

        public String getTitle() {
            return selectedUser == null ? "Please select user"
                    : selectedUser.name + '(' + selectedUser.account + '@' + selectedUser.login + ')';
        }

        public void setSelectedUser(final User selectedUser) {
            pcs.firePropertyChange("selectedUser", this.selectedUser, this.selectedUser = selectedUser);
            pcs.firePropertyChange("title", null, null);
        }
    }
}
```