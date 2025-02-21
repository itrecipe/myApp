import Header from "../components/Header/Header";

// import { Link } from "react-router-dom";

const Home = () => {
  return (
    <>
      <Header />
      
      <div className="container">

      {/* <Link to="/boards">게시판</Link><br/> */}

        <h1>Home - 메인 페이지</h1>
        <hr />
        Lorem, ipsum dolor sit amet consectetur adipisicing elit. Vero ratione
        laborum at aperiam exercitationem impedit ut quae non repudiandae,
        delectus nisi id explicabo quos veritatis voluptate doloribus tenetur
        sapiente perspiciatis. Lorem ipsum dolor sit amet consectetur
        adipisicing elit. Culpa possimus adipisci consectetur quasi officia
        minima veniam eos dolorem dicta. Facere, molestiae velit quae
        perspiciatis consectetur animi itaque tempora quod voluptas. Beatae
        alias ratione nulla laborum eveniet, magni maxime debitis minima
        nesciunt.
      </div>
    </>
  );
};

export default Home;
