document.querySelectorAll('input[name="us-citizen-radio"]').forEach(x => x.addEventListener('change', (event) => workChange(event)));
document.querySelectorAll('input[name="prev-employer-radio"]').forEach(x => x.addEventListener('change', (event) => prevEmployerChange(event)));
document.querySelectorAll('input[name="felony-radio"]').forEach(x => x.addEventListener('change', (event) => felonyChange(event)));

function workChange(event) {
    if(event.target.value === 'no')
    {
        document.getElementById('work-eligibility-div').style.display = 'flex';

        return;
    }

    document.getElementById('work-eligibility-div').style.display = 'none';
    document.querySelectorAll('input[name="work-eligibility-radio"]').forEach(x => x.checked = false);
}

function prevEmployerChange(event) {
    if(event.target.value === 'yes')
    {
        document.getElementById('prev-employer-div').style.display = 'flex';

        return;
    }

    document.getElementById('prev-employer-div').style.display = 'none';
    document.getElementById('prev-employer-date').value = '';
}

function felonyChange(event) {
    if(event.target.value === 'yes')
    {
        document.getElementById('felony-div').style.display = 'flex';

        return;
    }

    document.getElementById('felony-div').style.display = 'none';
    document.getElementById('felony-text').value = '';
}

function validateForm()
{
    return true;
}