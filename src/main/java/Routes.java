public class Routes {

    @WebRoute("/test1")
    public String test1() {
        String test1 = "test1method";
        return test1;
    }

    @WebRoute("/test2")
    public String test2() {
        String test2 = "test2method";
        return test2;
    }
}
