package com.xingqiao.coin.domain.vo;




public class BitcoinPriceResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String id;
        private Quote quote;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Quote getQuote() {
            return quote;
        }

        public void setQuote(Quote quote) {
            this.quote = quote;
        }

        public static class Quote {
            private Usd usd;

            public Usd getUsd() {
                return usd;
            }

            public void setUsd(Usd usd) {
                this.usd = usd;
            }

            public static class Usd {
                private double price;  // 比特币实时价格（美元）

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }
            }
        }
    }
}
