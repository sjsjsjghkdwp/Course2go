<template>
  <div class="profile">
    <!-- v-if -->
    <div class="profile-page">
      <span v-if="!nickname" class="my-profile">내 프로필</span>
      <span v-if="nickname" class="my-profile">{{nickname}}의 프로필</span>
      
      <span>
        <router-link to="/modify" v-if="!nickname">
          <i class="fas fa-cog"></i>
        </router-link>
      </span>
    </div>
    <ProfileCard :profileData="profileData" :nickname="nickname" :articleAmount="articleAmount" />
    <ProfileRoute :routeListData="routeListData" />
    <ProfilePlace :visitListData="visitListData" />
  </div>
</template>

<script>
import '@/assets/css/profile/profile.css';
import ProfileCard from '@/components/profile/ProfileCard.vue'
import ProfileRoute from '@/components/profile/ProfileRoute.vue'
import ProfilePlace from '@/components/profile/ProfilePlace.vue'
import { useRoute } from 'vue-router';
import { profile } from '@/compositions/profile.js';
import { jwtdecoder } from '@/compositions/utils/jwtdecoder.js'
export default {
  name: 'Profile',
  components: {
    ProfileCard,
    ProfileRoute,
    ProfilePlace,
  },
  setup() {
    const { myProfile, who, profileData, routeList, routeListData, visitList, visitListData, articleAmount } = profile();
    const route = useRoute();
    myProfile(route.query.nickname);
    routeList(route.query.nickname);
    visitList(route.query.nickname);
    console.log(routeListData)
    return { myProfile, who, profileData, routeList, routeListData, visitList, visitListData, articleAmount }
  },
  mounted(){
    this.nickname = this.$route.query.nickname
    const { mynickname } = jwtdecoder()
    if (this.nickname == mynickname) {
      this.nickname=""
    }
  },

  data(){
    return{
      nickname : "",
    }
  }

}
</script>