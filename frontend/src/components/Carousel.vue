<template>
  <div class="carouselBlock">
    <v-carousel cycle hide-delimiters>
      <v-carousel-item
        v-for="(item,i) in items"
        :key="i"
        :src="item.src"
        reverse-transition="fade-transition"
        transition="fade-transition"
      >
        <v-row class="title fill-height hidden-xs-only" align="center" justify="center">{{ item.title }}</v-row>
      </v-carousel-item>
    </v-carousel>
  </div>
</template>

<script>
export default {
  name: "Carousel",
  data() {
    return {
      items: [
        {
          src: require("../assets/images/img-carousel1.jpg"),
          title: "Discover the world"
        },
        {
          src: require("../assets/images/img-carousel2.jpg"),
          title: "Fly Your Dreams"
        },
        {
          src: require("../assets/images/img-carousel3.jpg"),
          title: "We go above and beyond"
        }
      ]
    };
  }
};
</script>