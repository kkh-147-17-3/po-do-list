<template>
    <div class="select-modal">
        <div class="month-year">
            <div class="month-year-selector">
                <v-btn flat>
                    <v-icon icon="mdi-chevron-left" @click="decreaseMonth"></v-icon>
                </v-btn>
                <label>{{ `${datePicker.currentYear} 년 - ${datePicker.currentMonth} 월`}}</label>
                <v-btn flat>
                    <v-icon icon="mdi-chevron-right" @click="increaseMonth"></v-icon>
                </v-btn>
            </div>
            <table>
                <tr>
                    <th>일</th>
                    <th>월</th>
                    <th>화</th>
                    <th>수</th>
                    <th>목</th>
                    <th>금</th>
                    <th>토</th>
                </tr>
                <tr v-for="(week, wIndex) in datePicker.dateInMonth" :key="wIndex" @click="selectDay">
                    <td v-for="(day, dIndex) in week" :key="dIndex" :class="{ holiday: day.isHoliday, selected: day.isSelected }">
                        {{ day.value !== "0" ? day.value : "" }}
                    </td>
                </tr>
            </table>
            <div :class="{ 'loader': !datePicker.isLoaded }"></div>
        </div>
        <div class="datepicker-buttons">
            <button type="button" class="btn btn-semiround" @click="closeDatepicker">취소</button>
            <button type="button" class="btn btn-action btn-semiround" @click="submitDate">확인</button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref,reactive } from "@vue/runtime-core";
const emit = defineEmits(['closeDatepicker','submitDate']);

const closeDatepicker = () => {
    emit('closeDatepicker');
}

const selectDay = (event:Event) => {
    const eventTarget = event.target as HTMLElement;
    let value = eventTarget.innerText;
    
    if (value === "") return;

    const valueInt = parseInt(value);
    datePicker.selectDay(valueInt);
}

interface DatePickerDay{
    value: string,
    isHoliday : boolean,
    isSelected : boolean,
}

class DatePicker {
    today:Date;
    currentYear:number;
    currentMonth:number;
    dateInMonth:DatePickerDay[][];
    holidays:[];
    isLoaded:boolean;
    selectedDate:{
                    year: number | null,
                    month: number | null,
                    day: number | null,
                }   


    constructor(date:Date) {
        this.today = date;
        this.currentYear = date.getFullYear();
        this.currentMonth = date.getMonth() + 1;
        this.dateInMonth = [];
        this.holidays = [];
        this.isLoaded = false;
        this.selectedDate = {
            year: null,
            month: null,
            day: null,
        }
    }
    init() {
        this.dateInMonth.length = 0;

        for (let i = 0; i < 6; i++) {
            let week = [];
            for (let i = 0; i < 7; i++) {
                week.push({
                    value: "",
                    isHoliday : false,
                    isSelected : false,
                });
            }
            this.dateInMonth.push(week);
        }
    }
    update() {
        // 이번달의 시작 요일을 계산함. 0인 경우 일요일, 6인 경우 토요일이다.
        let startDayofCurrentMonth = new Date(this.currentYear, this.currentMonth - 1, 1).getDay();
        let currentWeakIdx = 0;
        let currentDay = startDayofCurrentMonth;
        let currentDateNum = 1;


        let maxDate = this.getMaxDate();
        while (currentWeakIdx <= 5 && currentDateNum <= maxDate) {
            this.dateInMonth[currentWeakIdx][currentDay] = {
                value: (currentDateNum).toString(),
                isHoliday: false,
                isSelected : false,
            }
            currentDateNum++;
            currentDay++;
            if (currentDay == 7) {
                currentWeakIdx++;
                currentDay = 0;
            }
        }
    }
    decreaseMonth() {
        if (!this.isLoaded) return;

        this.currentMonth--;
        if (this.currentMonth == 0) {
            this.currentMonth = 12;
            this.currentYear--;
            const requestHolidays = this.getHolidays();
            requestHolidays.then(res=>res.json())
            .then(data=>{
                this.holidays = data.response.body.items.item;
                this.isLoaded = true;
                this.init();
                this.update();
                this.updateHolidays();
            })
            .catch(() => {
                console.log('휴일정보를 불러오는데 실패했습니다.');
            });
        }
        else{
            this.init();
            this.update();
            this.updateHolidays();
        }
    }
    increaseMonth() {
        if (!this.isLoaded) return;
        if (this.currentYear == new Date().getFullYear() + 2) return;

        this.currentMonth++;
        if (this.currentMonth == 13) {
            this.currentMonth = 1;
            this.currentYear++;
            const requestHolidays = this.getHolidays();
            requestHolidays.then(res=>res.json())
            .then(data=>{
                this.holidays = data.response.body.items.item;
                this.isLoaded = true;
                this.init();
                this.update();
                this.updateHolidays();
            })
            .catch(() => {
                console.log('휴일정보를 불러오는데 실패했습니다.');
            });
        }
        else{
            this.init();
            this.update();
            this.updateHolidays();
        }
    }
    getMaxDate() {
        let result;

        switch (this.currentMonth) {
            case 1:
                result = 31;
                break;
            case 2:
                if (this.isLeapYear()) {
                    result = 29;
                }
                else {
                    result = 28;
                }
                break;
            case 3:
                result = 31;
                break;
            case 4:
                result = 30;
                break;
            case 5:
                result = 31;
                break;
            case 6:
                result = 30;
                break;
            case 7:
                result = 31;
                break;
            case 8:
                result = 31;
                break;
            case 9:
                result = 30;
                break;
            case 10:
                result = 31;
                break;
            case 11:
                result = 30;
                break;
            case 12:
                result = 31;
                break;
            default:
                result = 30;
                break;
        }
        return result;
    }
    async getHolidays() {
        this.isLoaded = false;
        let serviceKey = 'S1yl3hQHoyz5%2FFbS%2BjDDcMj2zI1kkFrC2VMd0TkMct0Mbad0F3BfYH6C7m2e%2FmpQrGm0iZtjPz%2Fvg%2Bl3eMnuKA%3D%3D';
        let solYear = this.currentYear;
        let url = 'http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?';
        url+=`ServiceKey=${serviceKey}&`;
        url+=`solYear=${solYear}&`;
        url+=`_type=json&`;
        url+=`numOfRows=30`;

        return await fetch(url, {
            method: 'GET',
        })
    }
    updateHolidays(){
        this.holidays.forEach((holiday:any) => {
            const holidayMonth = parseInt(holiday.locdate.toString().slice(4,6))
            const holidayDate = holiday.locdate % 100;
            
            if(holidayMonth !== this.currentMonth) return;

            for(let week of this.dateInMonth){
                for (let day=0; day<week.length; day++){
                    if (parseInt(week[day].value) === holidayDate){
                        week[day].isHoliday = true;
                    }
                }
            }
        })
    }
    isLeapYear(){
    //4로 나눠떨어지면 윤년
    //4로 나눠 떨어지더라도 100으로 나눠떨어지면 평년
    //4로 나눠 떨어지고, 100으로 나눠떨어져도 400으로 나눠떨어지면 윤년

    if (this.currentYear % 4 != 0){
        return false;
    }

    if (this.currentYear % 100 != 0){
        return true;
    }

    if (this.currentYear % 400 != 0){
        return false;
    }

    return true;
    }
 
    selectDay(inputDay:number){
        this.dateInMonth.forEach(week=>{
            week.forEach(day=>{
                day.isSelected = false;
            })
        })

        this.selectedDate.year = this.currentYear;
        this.selectedDate.month = this.currentMonth;
        this.selectedDate.day = inputDay;
        this.dateInMonth.forEach(week=>{
            week.forEach(day=>{
                if(parseInt(day.value) === inputDay){
                    day.isSelected = true;
                }
            })
        })
    }
    getFormattedDate(){
        const year = this.selectedDate.year;
        const month = this.selectedDate.month;
        const day = this.selectedDate.day;

        if(year !== null && month !== null && day !== null){
            const monthStr = (month < 10) ? `0${month}` : `${month}`;
            const dayStr = (day < 10) ? `0${day}` : `${day}`;
            return `${year}-${monthStr}-${dayStr}T00:00`;
        }
        else{
            throw new Error('날짜를 선택해주세요');
        }
    }
}

const today = new Date();

const datePicker = reactive(new DatePicker(today));
datePicker.init();
const requestHolidays = datePicker.getHolidays();
requestHolidays
.then((res:any) =>res.json())
.then((data:any)=>{
    datePicker.holidays = data.response.body.items.item;
    datePicker.isLoaded = true;
    datePicker.init();
    datePicker.update();
    datePicker.updateHolidays();
})


const decreaseMonth = () => {
    datePicker.decreaseMonth();   
}

const increaseMonth = () => {
    datePicker.increaseMonth();
}

const errorMessage = ref("");

const submitDate = () => {
    try{
        const formattedDate = datePicker.getFormattedDate();
        const data = {
            formattedDate : formattedDate,
            dateInfo : datePicker.selectedDate,
        }
        emit('submitDate', data);
        emit('closeDatepicker');
    }
    catch (err:any){
        alert(err.message);
    }
}

</script>

<style scoped>
.month-year{
    position:relative;
}

.select-modal .month-year-selector {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 10px 0;
}

.select-modal label {
    width: 8rem;
    text-align: center;
}

.time-indicator{
    width:100%;
    display: flex;
    justify-content: space-between;
    align-items:center;
    margin: 10px 0;
}

.time-indicator > label{
    font-weight:var(--bold);
}

.time-indicator > input{
    font-size:18px;
    width:150px;
    text-align:center;
}

.hour-minute {
    display: flex;
    transition: max-height 0.2s;
    justify-content: center;
}

/* .hour-minute > ul{
    border: 1px solid #c4c4c4;
    border-radius:5px;
} */


.select-modal .hour-minute>ul {
    /*
    display: flex;
    flex-direction: column;
    justify-content: center;
    */
    height: 200px;
    overflow: scroll;
    overscroll-behavior: contain;
}

.select-modal .hour-minute>ul::-webkit-scrollbar {
    display: none;
}


.select-modal .hour-minute>ul:last-child {
    row-gap: 10px;
}

.select-modal .hour-minute>ul li {
    display: flex;
    width: 3rem;
    padding: 0.5rem 0.5rem;
    margin:0 0.5rem;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}

.hour-minute>ul li:hover{
    background-color: var(--light-grey1);
    border-radius: 5px;
}

.select-modal table td {
    text-align: center;
    width: 2rem;
    height: 2rem;
    cursor:pointer;
}

.select-modal table th {
    font-weight: 500;
    color: var(--grey2);
}

.select-modal table tr>td:nth-child(7) {
    color: var(--true-blue);
}

.select-modal tr>td:nth-child(1),
.select-modal .holiday {
    color: var(--red-theme);
}

.datepicker-buttons{
    display:flex; 
    justify-content: center; 
    column-gap: 10px; 
    padding: 30px 0;
}

.add-deco-img-left::before{
    width:16px;
    height:16px;
    flex: 0 0 16px;
    cursor: pointer;
}

.loader::before{
    font-size:5px;
}
</style>