package pl.qnet.sample;

public class ConsoleOutput implements Output {
    @Override
    public void sendOutputString(String inputString) {
        System.out.println(inputString);
    }
}
