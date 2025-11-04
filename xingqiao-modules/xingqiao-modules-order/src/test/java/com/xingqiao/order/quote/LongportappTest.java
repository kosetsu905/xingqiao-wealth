package com.xingqiao.order.quote;
import com.longport.*;
import com.longport.quote.QuoteContext;
import com.longport.quote.Security;
import com.longport.quote.SecurityListCategory;
import com.longport.quote.SubFlags;
import com.longport.trade.*;



public class LongportappTest {

    public static void main(String[] args) throws OpenApiException {
        test3();
    }

    private static void test3() throws OpenApiException {

        Config config = new ConfigBuilder("c602589735e0a6524ef81324c5198dbd", "d479a826c40458f5e7e6f84d42553add23cb6b040f043af239500d8c5126195b", "m_eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb25nYnJpZGdlIiwic3ViIjoiYWNjZXNzX3Rva2VuIiwiZXhwIjoxNzY5MTU1MjU3LCJpYXQiOjE3NjEzNzkyNTcsImFrIjoiYzYwMjU4OTczNWUwYTY1MjRlZjgxMzI0YzUxOThkYmQiLCJhYWlkIjoyMDk4MDE3NSwiYWMiOiJsYl9wYXBlcnRyYWRpbmciLCJtaWQiOjI0MDYxOTIzLCJzaWQiOiIwcUFiTHVVKzgrR3dESkIwSmZiSGdRPT0iLCJibCI6MCwidWwiOjAsImlrIjoibGJfcGFwZXJ0cmFkaW5nXzIwOTgwMTc1In0.5YosXS_7ilzLi0jl2Ul04YyajVNDWd9osjfL-jqMxPn14F1QYnIMUt-GOEWkAV6Tfg1WJ55IhkoKKeVKhkhukMMxXwyS_o-ewkUJRdTso6srQgTif_cE7Wes7v5tZCsRShsCDIDrbZJ7CBnYubnRy5pumTUvbRdGnrN8ku6i6agJLUEHj9p7YUwuhPaOKlNl8kX3NNfzCVtrQqdq2kNHT97j27N1_99Fni92QoskO5m8Je8Jplz4zsBG5MYY0g8qHq11MJGLKCjsigDHqzLU8p-bR6BrEEdRyuTrc3xCknYgTvbGOBcv-oVP15MC8FenihywBQWT7uSnZ4B1pqTb-6rrxJ7gZeJfi7mfc7U97puF6rFBcwqi1TjzKE9_82XDlo1f2VGnu2y1sH2PCyBQRImFHyd1cudse_pjRFn4IlEx_ZNlDsRjlvxuDpkO8vR5DEswRrj9dJmIiwyjFWOHNggdmdhs3G38U6MODzxHERForpcdcPSB4pGcd86bfzNOKNh9o4sZFdPK2X9GrG_7x7WPYnhLlDNKFZELLfeHPsd4tP35wGWVzrVhxFv4r980DrVgLv_BomMGBYNUMXiL25O7DXK5pX4JAufZ8cLUyNNwtmSPDGs-AZPHk9vHLnzRKVtpNNCmLNTbtnMQcFYKoF4u71XM8PDVzLflbjA7L-8").build();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            for (Security obj : ctx.getSecurityList(Market.CN, SecurityListCategory.Overnight).get()) {
                System.out.println(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() throws OpenApiException {

        Config config = new ConfigBuilder("c602589735e0a6524ef81324c5198dbd", "d479a826c40458f5e7e6f84d42553add23cb6b040f043af239500d8c5126195b", "m_eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb25nYnJpZGdlIiwic3ViIjoiYWNjZXNzX3Rva2VuIiwiZXhwIjoxNzY5MTU1MjU3LCJpYXQiOjE3NjEzNzkyNTcsImFrIjoiYzYwMjU4OTczNWUwYTY1MjRlZjgxMzI0YzUxOThkYmQiLCJhYWlkIjoyMDk4MDE3NSwiYWMiOiJsYl9wYXBlcnRyYWRpbmciLCJtaWQiOjI0MDYxOTIzLCJzaWQiOiIwcUFiTHVVKzgrR3dESkIwSmZiSGdRPT0iLCJibCI6MCwidWwiOjAsImlrIjoibGJfcGFwZXJ0cmFkaW5nXzIwOTgwMTc1In0.5YosXS_7ilzLi0jl2Ul04YyajVNDWd9osjfL-jqMxPn14F1QYnIMUt-GOEWkAV6Tfg1WJ55IhkoKKeVKhkhukMMxXwyS_o-ewkUJRdTso6srQgTif_cE7Wes7v5tZCsRShsCDIDrbZJ7CBnYubnRy5pumTUvbRdGnrN8ku6i6agJLUEHj9p7YUwuhPaOKlNl8kX3NNfzCVtrQqdq2kNHT97j27N1_99Fni92QoskO5m8Je8Jplz4zsBG5MYY0g8qHq11MJGLKCjsigDHqzLU8p-bR6BrEEdRyuTrc3xCknYgTvbGOBcv-oVP15MC8FenihywBQWT7uSnZ4B1pqTb-6rrxJ7gZeJfi7mfc7U97puF6rFBcwqi1TjzKE9_82XDlo1f2VGnu2y1sH2PCyBQRImFHyd1cudse_pjRFn4IlEx_ZNlDsRjlvxuDpkO8vR5DEswRrj9dJmIiwyjFWOHNggdmdhs3G38U6MODzxHERForpcdcPSB4pGcd86bfzNOKNh9o4sZFdPK2X9GrG_7x7WPYnhLlDNKFZELLfeHPsd4tP35wGWVzrVhxFv4r980DrVgLv_BomMGBYNUMXiL25O7DXK5pX4JAufZ8cLUyNNwtmSPDGs-AZPHk9vHLnzRKVtpNNCmLNTbtnMQcFYKoF4u71XM8PDVzLflbjA7L-8").build();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            ctx.setOnQuote((symbol, quote) -> {
                System.out.printf("%s\t%s\n", symbol, quote);
            });
            ctx.subscribe(new String[] { "600036.SH", "AAPL.US", "TSLA.US", "NFLX.US" }, SubFlags.Quote, true).get();
            Thread.sleep(30000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void test1() throws OpenApiException {
        Config config = new ConfigBuilder("c602589735e0a6524ef81324c5198dbd", "d479a826c40458f5e7e6f84d42553add23cb6b040f043af239500d8c5126195b", "m_eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb25nYnJpZGdlIiwic3ViIjoiYWNjZXNzX3Rva2VuIiwiZXhwIjoxNzY5MTU1MjU3LCJpYXQiOjE3NjEzNzkyNTcsImFrIjoiYzYwMjU4OTczNWUwYTY1MjRlZjgxMzI0YzUxOThkYmQiLCJhYWlkIjoyMDk4MDE3NSwiYWMiOiJsYl9wYXBlcnRyYWRpbmciLCJtaWQiOjI0MDYxOTIzLCJzaWQiOiIwcUFiTHVVKzgrR3dESkIwSmZiSGdRPT0iLCJibCI6MCwidWwiOjAsImlrIjoibGJfcGFwZXJ0cmFkaW5nXzIwOTgwMTc1In0.5YosXS_7ilzLi0jl2Ul04YyajVNDWd9osjfL-jqMxPn14F1QYnIMUt-GOEWkAV6Tfg1WJ55IhkoKKeVKhkhukMMxXwyS_o-ewkUJRdTso6srQgTif_cE7Wes7v5tZCsRShsCDIDrbZJ7CBnYubnRy5pumTUvbRdGnrN8ku6i6agJLUEHj9p7YUwuhPaOKlNl8kX3NNfzCVtrQqdq2kNHT97j27N1_99Fni92QoskO5m8Je8Jplz4zsBG5MYY0g8qHq11MJGLKCjsigDHqzLU8p-bR6BrEEdRyuTrc3xCknYgTvbGOBcv-oVP15MC8FenihywBQWT7uSnZ4B1pqTb-6rrxJ7gZeJfi7mfc7U97puF6rFBcwqi1TjzKE9_82XDlo1f2VGnu2y1sH2PCyBQRImFHyd1cudse_pjRFn4IlEx_ZNlDsRjlvxuDpkO8vR5DEswRrj9dJmIiwyjFWOHNggdmdhs3G38U6MODzxHERForpcdcPSB4pGcd86bfzNOKNh9o4sZFdPK2X9GrG_7x7WPYnhLlDNKFZELLfeHPsd4tP35wGWVzrVhxFv4r980DrVgLv_BomMGBYNUMXiL25O7DXK5pX4JAufZ8cLUyNNwtmSPDGs-AZPHk9vHLnzRKVtpNNCmLNTbtnMQcFYKoF4u71XM8PDVzLflbjA7L-8").build();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (AccountBalance obj : ctx.getAccountBalance().get()) {
                System.out.println(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
