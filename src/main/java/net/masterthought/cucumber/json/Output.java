package net.masterthought.cucumber.json;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Output {

    private final String[] messages;

    public Output(String[] messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
