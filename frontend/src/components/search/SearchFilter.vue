<template>
  <details open class="search-filter">
    <summary><span class="filter-title">검색조건</span></summary>
    <hr />
    <div class="content" style="text-align:center;">
      <div class="filter-namebox" style="display:inline-block;">
        <span class="filter-name" v-show="article == 1" @click="clickArticle">동선 + 장소</span>
        <span class="filter-name active" v-show="article == 2" @click="clickArticle">
          동선 게시물
        </span>
        <span class="filter-name active" v-show="article == 3" @click="clickArticle"
          >장소 게시물</span
        >
      </div>
      &nbsp;&nbsp;&nbsp;
      <div class="filter-namebox" style="display:inline-block;">
        <span class="filter-name" v-show="like == 1" @click="clickLike">등록시간순</span>
        <span class="filter-name active" v-show="like == 2" @click="clickLike"
          >좋아요낮은순</span
        >
        <span class="filter-name active" v-show="like == 3" @click="clickLike"
          >좋아요높은순</span
        >
      </div>
      <div v-show="article != 3" id="filter-date">
        <hr />
        <div class="filter-date" style="visibility:block;">
          <div>
            <label for="start">start date</label>
            <div>
              <input type="date" name="start" v-model="startDate" />
            </div>
          </div>
          <div>
            <label for="end">end date</label>
            <div>
              <input type="date" name="end" v-model="endDate" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="search-applybox" style="text-align:center;">
      <br />
      <button class="search-apply" @click="filterApply">적용</button>
      <button class="search-cancel" @click="filterCancel">취소</button>
    </div>
  </details>
</template>

<script>
import "@/components/css/search/search-filter.css";
import { ref } from "vue";

export default {
  name: "SearchFilter",
  setup(_, { emit }) {
    const article = ref(1);
    const like = ref(1);
    const startDate = ref(new Date());
    const endDate = ref(new Date());

    const clickArticle = () => {
      article.value = article.value < 3 ? article.value + 1 : 1;
    };
    const clickLike = () => {
      like.value = like.value < 3 ? like.value + 1 : 1;
    };
    const filterApply = () => {
      emit("getFilter", {
        article: article,
        like: like,
        startDate: startDate,
        endDate: endDate,
      });
    };
    const filterCancel = () => {
      article.value = 1
      like.value = 1
      startDate.value = new Date()
      endDate.value = new Date()
      emit("getFilter", {
        article: article,
        like: like,
        startDate: startDate,
        endDate: endDate,
      });
    };

    return {
      article,
      like,
      startDate,
      endDate,
      clickArticle,
      clickLike,
      filterApply,
      filterCancel
    };
  },
};
</script>
