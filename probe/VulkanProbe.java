import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVulkan;

public class VulkanProbe {
    public static void main(String[] args) {
        System.out.println("1) before glfwInit():        " + GLFWVulkan.glfwVulkanSupported());
        boolean init = GLFW.glfwInit();
        System.out.println("2) glfwInit() result:        " + init);
        System.out.println("3) after glfwInit():         " + GLFWVulkan.glfwVulkanSupported());
        GLFW.glfwTerminate();
    }
}
