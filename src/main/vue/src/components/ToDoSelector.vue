<template>
    <div class="select-modal">
        <Transition name="parent">
            <div class="parent" v-show="categories.isParentLoaded">
                <div v-for="(item, index) in categories.parent" 
                :key="index"
                @click="parentClickHandler(item)">
                <v-img width="100" :src="(item.imageUrl) ? `/image/category/${item.imageUrl}` : `/image/category/beer.png` "></v-img>
                    {{ item.name }}
                </div>
            <div>직접입력</div>     
        </div>
        </Transition>
        <Transition name="child">
            <div class="child" v-show="categories.isChildLoaded">
                <div v-for="(item, index) in categories.child" 
                    :key="index"
                    @click="childClickHandler(item)">
                    <v-img width="100px" :src="(item.imageUrl) ? `/image/category/${item.imageUrl}` : `/image/category/beer.png` "></v-img>
                    {{ item.name }}
                </div>
                <div>직접입력</div>
            </div>
        </Transition>
        
    </div>
</template>



<script setup lang="ts">
import {ref, reactive, watch} from 'vue';

export interface Category {
    id:number,
    name:string,
    imageUrl:string,
}

export interface CurrSelected{
    parent: Category | null,
    child: Category | null,
}

const props = defineProps(['opened']);

const parent:Array<Category> = [];
const child: Array<Category> = [];

const categories = reactive({
    parent,
    isParentLoaded: false,
    child,
    isChildLoaded:false,
})

function initParent() {
    fetch('/api/category/parent')
    .then(res=>res.json())
    .then((data:Array<Category>) =>{
        data.forEach(category=>parent.push(category));
        categories.isParentLoaded = true;
    })
}
initParent();

function initChild(id: number) {
    fetch('/api/category/child?' + new URLSearchParams({
        parentId: id.toString()
    }))
    .then(res=>res.json())
    .then((data:Array<Category>) =>{
        data.forEach(category=>child.push(category));
        categories.isParentLoaded=false;
        categories.isChildLoaded=true;
    })
}


const currSelected: CurrSelected = reactive({
    parent: null,
    child: null,
})

const emits = defineEmits(['todoSelected']);

const parentClickHandler = (item:Category) => {
    currSelected.child = null;
    currSelected.parent = item;
}

const childClickHandler = (item:Category) =>{
    currSelected.child = item;
    emits('todoSelected', currSelected);
}

watch(currSelected,()=>{
    if(currSelected.parent){
        initChild(currSelected.parent.id);
    }
})

</script>

<style lang="scss" scoped>
.parent-enter-active,
.parent-leave-active {
  transition: all 0.5s ease;
}

.parent-enter-from,
.parent-leave-to {
  transform:translateX(-100%);
  opacity: 0;
}


.child-enter-active,
.child-leave-active {
  transition: all 0.5s ease;
}

.child-enter-to,
.child-leave-from {
  transform:translateX(-100%);
  opacity: 1;
}

.select-modal{
    height:400px; 
    display:flex; 
    overflow-x:hidden;
}

.parent, .child{
    display:flex;
    flex-wrap: wrap;
}

.child{
    display:flex;
}
</style>