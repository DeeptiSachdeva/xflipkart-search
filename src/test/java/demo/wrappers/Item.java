package demo.wrappers;

   public class Item{
         String title;
         String imageUrl;
         int reviewsCount;

        public Item(String title, String imageUrl, int reviewsCount) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.reviewsCount = reviewsCount;
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getReviewsCount() {
            return reviewsCount;
        }
    }

