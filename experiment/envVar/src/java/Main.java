import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // 명령어 설정 - 환경 변수를 사용하여 출력
        processBuilder.command("bash", "-c", "echo $MY_ENV_VAR");

        try {
            Process process = processBuilder.start();

            // 출력 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String envVarName="MY_ENV_VAR";
        String envVarValue=System.getenv(envVarName);

        String envVarName2="TEST";
        String envVarValue2=System.getenv(envVarName2);
        System.out.println("env1 :"+envVarValue);
        System.out.println("env2 :"+envVarValue2);
        


    }
}
