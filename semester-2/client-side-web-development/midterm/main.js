window.onload = function() {
    var currentPage = window.location.pathname.split('/').pop();

    if (currentPage === 'index.html') {
        let user = prompt("Please enter your name:");

        if(user)
        {
            let topSection = document.createElement('section');
            let title = document.createElement('h2');
            title.textContent = `Welcome ${user}`;
    
            topSection.appendChild(title);
    
            document.body.prepend(topSection);
        }
    }
}

function showDiscontinuedProducts()
{
    let dicsProducts = ['Nike Air Mag – A limited-edition shoe inspired by the "Back to the Future" movies, featuring self-lacing technology.',
        'Nike Shox R4 – Popular in the early 2000s, this shoe had a distinctive column-based cushioning system.',
        'Nike Air Zoom Generation – LeBron James first signature shoe, released in 2003, with a classic design that is no longer produced.',
        'Nike Huarache 2K4 – A performance basketball shoe from the mid-2000s, notable for its unique design and lightweight feel.',
        'Nike Air Rift – Released in the mid-90s, the Air Rift had a split-toe design and was part of a bold, unconventional look that didn’t last long.'];
    let productsSection = document.getElementById('courses');
    let title = document.createElement('h2');
    let ul = document.createElement('ul');

    title.textContent = 'Discontinued Products';
    dicsProducts.forEach(x => {
        let li = document.createElement('li');
        li.textContent = x;
        ul.appendChild(li);
    });

    productsSection.appendChild(title);
    productsSection.appendChild(ul);
}

function updateTxtColor()
{
    document.getElementById('mission').style.color = 'red';
}

function validateForm()
{
    return true;
}