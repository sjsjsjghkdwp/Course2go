<template>
  <div class="route-card">
    <router-link :to="{ name: 'RouteArticle', query: { bid: route.boardResponse.bid }}">
        {{route.boardResponse.boardTitle}}
    </router-link>
  </div>
</template>

<script>
import "@/components/css/profile/routeCard.css"
export default {
  name: 'RouteCard',
  props:  {
    route: {
      type: Object
    }
  },
}
</script>