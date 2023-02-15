<template>
    <div class="timepicker select-modal">
        <div class="timepicker__indicator">
            <label for="">시간</label>
            <input class="input" type="text" name="" id="" placeholder="시간을 선택해주세요" :value="showSelectedTime" readonly>
        </div>
        <div class="timepicker__hour-minute">
            <ul @click="selectAmPm" class="am-pm">
                <li :class="{ selected: times.amPm[0].isSelected }"> {{ times.amPm[0].value }} </li>
                <li :class="{ selected: times.amPm[1].isSelected }">{{ times.amPm[1].value }}</li>
            </ul>
            <ul @click="selectHour" class="hour">
                <li v-for="(hour, index) in times.hours" :key="index" :class="{ selected: hour.isSelected }"> {{
                    hour.value
                }} </li>
            </ul>
            <ul @click="selectMinute" class="minute">
                <li v-for="(minute, index) in times.minutes" :key="index"
                    :class="{ selected: minute.isSelected }"> {{ minute.value }} </li>
            </ul>
        </div>
        <div class="timepicker__buttons">
            <button type="button" class="btn btn-semiround" @click="closeDatepicker">취소</button>
            <button type="button" class="btn btn-action btn-semiround" @click="submitTime">확인</button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { reactive,computed } from 'vue';

export interface Time {
    value: string,
    isSelected : boolean,
}

export interface SelectedTime{
    isAm: boolean,
    hour: number,
    minute: number,
}

const emits = defineEmits(['close','submit']);
const props = defineProps({
    value:Date,
})

const currSelected:SelectedTime = reactive({
    isAm: true,
    hour: 0,
    minute:0,
})

const taskTime = props.value;

if (taskTime){
    let valueHour = taskTime.getHours()
    let valueMinute = taskTime.getMinutes();
    let isValueAm = valueHour >= 12 ? false : true;
    valueHour = valueHour >= 12 ? valueHour - 12 : valueHour;

    currSelected.isAm = isValueAm;
    currSelected.hour = valueHour;
    currSelected.minute = valueMinute;
}



const times:{
    amPm:Time[],minutes:Time[],hours:Time[],
} = reactive({
    amPm: [],
    minutes:[],
    hours:[],
});



function initHour() {
    times.hours.length = 0;
    for (let i = 0; i < 12; i++){
        let hour = (i+1);
        let hourStr = hour < 10 ? `0${hour}` : hour.toString();
        times.hours.push({
            value : hourStr,
            isSelected : false,
        });
    }
    times.hours[currSelected.hour-1].isSelected = true;

}

function initMinute(){
    times.minutes.length = 0;
    for (let i = 0; i < 12; i++){
        let minute = i * 5;
        let minuteStr = minute < 10 ? `0${minute}` : minute.toString();
        times.minutes.push({
            value : minuteStr,
            isSelected : false,
        })
    }
    times.minutes[currSelected.minute/5].isSelected = true;

}

function initAmPm(){
    times.amPm.length = 0;
    times.amPm.push({
        value : '오전',
        isSelected : false,
    })
    times.amPm.push({
        value : '오후',
        isSelected : false,
    })

    if(currSelected.isAm){
        times.amPm[0].isSelected = true;
    }
    else{
        times.amPm[1].isSelected = true;
    }
}

initHour();
initMinute();
initAmPm();

function selectAmPm(e:Event){
    const eventTarget = e.target as HTMLElement;
    let value = eventTarget.innerText;
    if (value=='오전'){
        currSelected.isAm = true;
    }
    else{
        currSelected.isAm = false;
    }
    initAmPm();
}

function selectHour(e:Event){
    const eventTarget = e.target as HTMLElement;
    let value = parseInt(eventTarget.innerText);
    times.hours[value-1].isSelected = true;
    currSelected.hour = value;
    initHour();
}

function selectMinute(e:Event){
    const eventTarget = e.target as HTMLElement;
    let value = parseInt(eventTarget.innerText);
    times.minutes[value/5].isSelected = true;
    currSelected.minute = value;
    initMinute();
}

const showSelectedTime = computed(()=>{
    let amPm, hour, minute = null;

    amPm = (currSelected.isAm) ? '오전' : '오후';

    if (currSelected.hour) {
        let selectedHour = currSelected.hour;
        hour = (selectedHour < 10) ? `0${selectedHour}` : selectedHour;
    }
    else {
        hour = "   ";
    }

    if (currSelected.minute != null) {
        let selectedMinute = currSelected.minute;
        minute = (selectedMinute < 10) ? `0${selectedMinute}` : selectedMinute;
    }
    else {
        minute = "   ";
    }

    return `${amPm} ${hour} : ${minute}`;
})

function closeDatepicker(){
    emits('close')
}

function submitTime(){
    emits('submit',currSelected);
}



</script>

<style lang="scss" scoped>

.timepicker__hour-minute {
    display: flex;
    transition: max-height 0.2s;
    justify-content: center;
    height:150px;
    ul{
        overflow: scroll;
        overscroll-behavior: contain;
        &::-webkit-scrollbar{
            display: none;
        }
        &:last-child{
            row-gap: 10px;
        }
        li{
            display: flex;
            width: 3rem;
            padding: 0.5rem 0.5rem;
            margin:0 0.5rem;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            &:hover{
                background-color: var(--light-grey1);
                border-radius: 5px;
            }
        }
        
    }
}

.timepicker__buttons{
    display:flex; 
    justify-content: center; 
    column-gap: 10px; 
    padding: 30px 0;
}

</style>
